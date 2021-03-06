package com.example.androidproject

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Camera
import android.graphics.drawable.BitmapDrawable
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.location.Location
import android.location.LocationManager
import android.os.AsyncTask
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.ClusterRenderer
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_map.*
import kotlinx.android.synthetic.main.activity_report_list.*
import kotlinx.android.synthetic.main.search_bar.view.*
import org.json.JSONArray
import org.json.JSONObject
import java.net.URL

class MapActivity : AppCompatActivity(),GoogleMap.OnMarkerClickListener{

    //마커를 눌렀을 때, 해당 화장실의 신고 내역을 보여주는 화면으로 전환해주는 함수
    override fun onMarkerClick(m:Marker): Boolean {
        val intent =Intent(this@MapActivity, ReportListActivity::class.java)
        //getJSONObject(인덱스) 어떤 인덱스가 들어가야할지, marker class의 정보를 활용해야 한다.
        //marker class의 title 속성에 서울시 API의 주키인 POI_ID 값을 넣어서 활용함
        val id  = m.title
        intent.putExtra("id", id)
        startActivity(intent)
        return true
    }

    val PERMISSIONS = arrayOf( //권한에 대한 목록
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    val REQUEST_PERMISSION_CODE = 1
    val DEFAULT_ZOOM_LEVEL = 17f

    //애뮬레이터에서 gps가 구글 본사로 찍히므로, 디폴트 위치를 넣어줌
    //처음 맵 액티비티에 갔을 때, 처음으로 카메라가 비춰지는 장소인 디폴트 위치로
    //아무 화장실을 넣음
    val CITY_HALL = LatLng(37.4923861462717, 126.909832374686)

    var googleMap: GoogleMap? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        mapView.onCreate(savedInstanceState)

        if (hasPermissions()) {
            initMap()
        } else {
            ActivityCompat.requestPermissions(this, PERMISSIONS, REQUEST_PERMISSION_CODE)
        }

        myLocationButton.setOnClickListener { onMyLocationButtonClick() }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        initMap()
    }

    fun hasPermissions(): Boolean {
        for (permission in PERMISSIONS) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }
        return true
    }

    var clusterManager:ClusterManager<MyItem>?=null //ClusterManager 변수 선언
    var clusterRenderer:com.example.androidproject.ClusterRenderer?=null //ClusterRenderer 변수 선언

    @SuppressLint("MissingPermission")
    fun initMap() {
        mapView.getMapAsync {
            clusterManager= ClusterManager(this,it)
            clusterRenderer= ClusterRenderer(this,it,clusterManager)

            it.setOnCameraIdleListener(clusterManager)
            it.setOnMarkerClickListener(clusterManager)
            it.setOnMarkerClickListener(this)
        }
        mapView.getMapAsync {
            googleMap = it
            it.uiSettings.isMyLocationButtonEnabled = false
            when {
                hasPermissions() -> {
                    it.isMyLocationEnabled = true
                    it.moveCamera(
                        CameraUpdateFactory.newLatLngZoom(
                            getMyLocation(),
                            DEFAULT_ZOOM_LEVEL
                        )
                    )
                }
                else -> {
                    it.moveCamera(CameraUpdateFactory.newLatLngZoom(CITY_HALL, DEFAULT_ZOOM_LEVEL))
                }
            }
        }
    }


    @SuppressLint("MissingPermission")
    fun getMyLocation(): LatLng {
        val locationProvider: String = LocationManager.GPS_PROVIDER
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val lastKnownLocation: Location = locationManager.getLastKnownLocation(locationProvider)

        return LatLng(lastKnownLocation.latitude, lastKnownLocation.longitude)
    }

    //우측 하단 동그라미 버튼을 누르면 현재위치로 카메라가 이동함
    //에뮬레이터 상에선 구글 본사로 찍힙니다.
    fun onMyLocationButtonClick() {
        when {
            hasPermissions() -> googleMap?.moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                    getMyLocation(),
                    DEFAULT_ZOOM_LEVEL
                )
            )
            else -> Toast.makeText(
                applicationContext,
                "위치 사용 권한 설정에 동의해주세요",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    // 서울 열린 데이터 광장에서 발급받은 API 키를 입력
    val API_KEY = "4453504a7763686532394f6f6d784e"
    // 앱이 비활성화될때 백그라운드 작업도 취소하기 위한 변수 선언
    var task: ToiletReadTask? = null
    // 서울시 화장실 정보 집합을 저장할 Array 변수. 검색을 위해 저장
    var toilets = JSONArray()
    val itemMap = mutableMapOf<JSONObject,MyItem>()

    // JSONArray 를 병합하기 위해 확장함수 사용
    fun JSONArray.merge(anotherArray: JSONArray) {
        for (i in 0 until anotherArray.length()) {
            this.put(anotherArray.get(i))
        }
    }

    // 화장실 정보를 읽어와 JSONObject 로 반환하는 함수
    fun readData(startIndex: Int, lastIndex: Int): JSONObject {
        val url =
            URL("http://openAPI.seoul.go.kr:8088" + "/${API_KEY}/json/SearchPublicToiletPOIService/${startIndex}/${lastIndex}")
        val connection = url.openConnection()
        val data = connection.getInputStream().readBytes().toString(charset("UTF-8"))
        return JSONObject(data)
    }

    // 화장실 데이터를 읽어오는 AsyncTask
    inner class ToiletReadTask : AsyncTask<Void, JSONArray, String>() {
        // 데이터를 읽기 전에 기존 데이터 초기화
        override fun onPreExecute() {
            // 구글맵 마커 초기화
            googleMap?.clear()
            // 화장실 정보 초기화
            toilets = JSONArray()
            itemMap.clear()
        }

        override fun doInBackground(vararg params: Void?): String {
            // 서울시 데이터는 최대 1000 개씩 가져올수 있기 때문에
            // step 만큼 startIndex 와 lastIndex 값을 변경하며 여러번 호출해야 함.
            val step = 1000
            var startIndex = 1
            var lastIndex = step
            var totalCount = 0
            do {
                // 백그라운드 작업이 취소된 경우 루프를 빠져나간다.
                if (isCancelled) break
                // totalCount 가 0 이 아닌 경우 최초 실행이 아니므로 step 만큼 startIndex 와 lastIndex 를 증가
                if (totalCount != 0) {
                    startIndex += step
                    lastIndex += step
                }
                // startIndex, lastIndex 로 데이터 조회
                val jsonObject = readData(startIndex, lastIndex)
                // totalCount 를 가져온다.
                totalCount = jsonObject.getJSONObject("SearchPublicToiletPOIService")
                    .getInt("list_total_count")
                // 화장실 정보 데이터 집합을 가져온다.
                val rows =
                    jsonObject.getJSONObject("SearchPublicToiletPOIService").getJSONArray("row")
                // 기존에 읽은 데이터와 병합
                toilets.merge(rows)
                // UI 업데이트를 위해 progress 발행
                publishProgress(rows)
            } while (lastIndex < totalCount) // lastIndex 가 총 개수보다 적으면 반복한다
            return "complete"
        }

        // 데이터를 읽어올때마다 중간중간 실행
        override fun onProgressUpdate(vararg values: JSONArray?) {
            // vararg 는 JSONArray 파라미터를 가변적으로 전달하도록 하는 키워드
            // 인덱스 0의 데이터를 사용
            val array = values[0]
            array?.let {
                for (i in 0 until array.length()) {
                    // 마커 추가
                    var toiletTemp = array.getJSONObject(i)

                    addMarkers(toiletTemp)
                }
            }
            clusterManager?.cluster()
        }

        override fun onPostExecute(result: String?) {
            val textList= mutableListOf<String>()

            for(i in 0 until toilets.length()){
                val toilet = toilets.getJSONObject(i)
                textList.add(toilet.getString("FNAME"))
            }

            val adapter=ArrayAdapter<String>(
                this@MapActivity, android.R.layout.simple_dropdown_item_1line,textList
            )

            searchBar.autoCompleteTextView.threshold=1
//            searchBar.autoCompleteTextView = adapter
            searchBar.autoCompleteTextView.setAdapter(adapter)
        }
    }

    fun JSONArray.findByChildProperty(propertyName: String,value:String):JSONObject?{
        for(i in 0 until length()){
            val obj = getJSONObject(i)
            if(value == obj.getString(propertyName)) return obj
        }
        return null
    }

    // 앱이 활성화될때 서울시 데이터를 읽어옴
    override fun onStart() {
        super.onStart()
        task?.cancel(true)
        task = ToiletReadTask()
        task?.execute()

        searchBar.imageView.setOnClickListener {
            val keyword = searchBar.autoCompleteTextView.text.toString()
            if(TextUtils.isEmpty(keyword)) return@setOnClickListener

            toilets.findByChildProperty("FNAME",keyword)?.let {
                val myItem = itemMap[it]
                val marker=clusterRenderer?.getMarker(myItem)

                marker?.showInfoWindow()

                googleMap?.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        LatLng(it.getDouble("Y_WGS84"),it.getDouble("X_WGS84")),DEFAULT_ZOOM_LEVEL))
                clusterManager?.cluster()
            }
            searchBar.autoCompleteTextView.setText("")
        }
    }

    // 앱이 비활성화 될때 백그라운드 작업 취소
    override fun onStop() {
        super.onStop()
        task?.cancel(true)
        task = null
    }


    // 마커를 추가하는 함수
    fun addMarkers(toilet: JSONObject) {
        //해당 화장실의 신고 개수를 세는 변수
        var cnt = 0
        //제이슨 객체에 저장된 POI_ID 값은 각 화장실의 고유 id 정보이다.
        val id:String = toilet.getString("POI_ID")
        //파이어 스토어에서 객체를 생성하여, Reports 컬렉션에서, 위에서 넣어준 id 값을
        //id 필드 값으로 가지느 신고 내역들을 가져오게 한다.
        FirebaseFirestore.getInstance()
            .collection("Reports")
            .whereEqualTo("id",id).get()
            .addOnSuccessListener {
                //해당 쿼리가 잘 실행되어서, 결과가 왔다면
                //해당 뭐리 스냅샷의 크기, 즉 문서의 개수를 cnt에 넣어준다.
                cnt = it.size()
                //println(cnt)


                // 화장실 이미지로 사용할 Bitmap을 cnt변수에 따라서 초기화 해준다
                val bitmap by lazy {
                    if(cnt == 0){ //신고가 0개라면 초록색
                        val drawable = resources.getDrawable(R.drawable.green_gps) as BitmapDrawable
                        Bitmap.createScaledBitmap(drawable.bitmap, 64, 64, false)
                    }
                    else if((0 < cnt) && (cnt <=2)){ //신고가 1개~2개라면 주황색
                        val drawable = resources.getDrawable(R.drawable.yellow_gps) as BitmapDrawable
                        Bitmap.createScaledBitmap(drawable.bitmap, 64, 64, false)
                    }
                     else{ //신고가 3개 이상이라면 빨강색
                        val drawable = resources.getDrawable(R.drawable.red_gps) as BitmapDrawable
                        Bitmap.createScaledBitmap(drawable.bitmap, 64, 64, false)
                    }

                }
                val item = MyItem(
                    LatLng(toilet.getDouble("Y_WGS84"),toilet.getDouble("X_WGS84")),
                    toilet.getString("POI_ID"),
                    toilet.getString("ANAME"),
                    BitmapDescriptorFactory.fromBitmap(bitmap)
                )
                clusterManager?.addItem(
                    MyItem(
                        LatLng(toilet.getDouble("Y_WGS84"), toilet.getDouble("X_WGS84")),
                        toilet.getString("POI_ID"),
                        toilet.getString("ANAME"),
                        BitmapDescriptorFactory.fromBitmap(bitmap)
                    )
                )
                itemMap.put(toilet,item)


        }



    }
}