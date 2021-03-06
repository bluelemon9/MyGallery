package com.example.mygallery

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.noButton
import org.jetbrains.anko.toast
import org.jetbrains.anko.yesButton
import kotlin.concurrent.timer

class MainActivity : AppCompatActivity() {

    private val REQUEST_READ_EXTERNAL_STORAGE = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //권한이 부여되었는지 확인
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            // 권한이 허용되지 않음
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)){
                // 이전에 이미 권한이 거부되었을 때 설명
                alert("사진 정보를 얻으려면 외부 저장소 권한이 필수로 필요합니다.", "권한이 필요한 이유"){
                    yesButton {
                        // 권한 요청
                        ActivityCompat.requestPermissions(this@MainActivity,
                            arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                            REQUEST_READ_EXTERNAL_STORAGE)
                    }
                    noButton { }
                }.show()
            } else{
                // 권한 요청
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                    REQUEST_READ_EXTERNAL_STORAGE)
            }
        } else{
            // 권한이 이미 허용됨
            getAllPhotos()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            REQUEST_READ_EXTERNAL_STORAGE -> {  // grantResults[] 에는 요청한 권한들의 결과가 전달됨
                if((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)){
                    // 권한 허용됨
                    getAllPhotos()
                } else{
                    // 권한 거부
                    toast("권한 거부 됨")
                }
                return
            }
        }
    }

    // 모든 사진 정보 가져오기
    private fun getAllPhotos(){
        val cursor = contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,    // 외부 저장소에 저장된 데이터를 가리키는 URI 형태로 지정
            null,       //가져올 항목 배열
            null,        // 데이터를 가져올 조건 (null: 전체 데이터)
            null,    // 조건
            MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC")     // 찍은 날짜 내림차순


        val fragments = ArrayList<Fragment>()   // 프래그먼트를 아이템으로 하는 ArrayList 생성
        // 사진 정보 로그로 표시
        if(cursor != null) {                   // 사진이 있다면
            while (cursor.moveToNext()) {
                // 사진 경로 Uri 가져오기
                var uri = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA))
                Log.d("MainActivity", uri)
                fragments.add(PhotoFragment.newInstance(uri))
            }
            cursor.close()
        }

        // 어댑터
        val adapter = MyPagerAdapter(supportFragmentManager)
        adapter.updateFragment(fragments)   // 프래그먼트 리스트 전달
        viewPager.adapter = adapter         // 어댑터를 viewPager에 설정

        // 슬라이드쇼(타이머 코드)
        timer(period = 3000){   // 3초마다 실행
            runOnUiThread {
                if(viewPager.currentItem < adapter.count - 1){          // 현재 페이지가 마지막 페이지가 아니라면
                    viewPager.currentItem = viewPager.currentItem + 1   // 다음 페이지로 변경
                } else{
                    viewPager.currentItem = 0                           // 마지막 페이지라면 첫 페이지로 변경
                }
            }
        }
    }
}