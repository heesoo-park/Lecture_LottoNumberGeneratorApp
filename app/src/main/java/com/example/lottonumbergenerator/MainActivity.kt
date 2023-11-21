package com.example.lottonumbergenerator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible

class MainActivity : AppCompatActivity() {

    // 해당 객체들을 사용할 때 초기화되도록 by lazy로 선언
    private val clearBtn: Button by lazy { findViewById(R.id.clear_btn) }
    private val addBtn: Button by lazy { findViewById(R.id.add_btn) }
    private val autoStartBtn: Button by lazy { findViewById(R.id.autoStart_btn) }
    private val numberPicker: NumberPicker by lazy { findViewById(R.id.number_picker) }

    // 화면에 나오게 될 공 6개를 한번에 관리할 수 있도록 리스트로 묶어 by lazy로 선언
    private val numTextViewList: List<TextView> by lazy {
        listOf<TextView>(
            findViewById(R.id.num1_text),
            findViewById(R.id.num2_text),
            findViewById(R.id.num3_text),
            findViewById(R.id.num4_text),
            findViewById(R.id.num5_text),
            findViewById(R.id.num6_text),
        )
    }

    // 자동 생성이 실행되었는지 여부
    private var didRun = false
    // 선택된 숫자들이 들어가있을 공간
    private val pickNumberSet = hashSetOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // NumberPicker의 최소, 최대 숫자 지정
        // 지정하지 않으면 그냥 이미지처럼 0만 떠있음
        numberPicker.minValue = 1
        numberPicker.maxValue = 45

        // 자동 생성 버튼 클릭 이벤트
        initRunButton()
        // 번호 추가 버튼 클릭 이벤트
        initAddButton()
        // 초기화 버튼 클릭 이벤트
        initClearButton()
    }

    private fun initClearButton() {
        clearBtn.setOnClickListener {
            pickNumberSet.clear()
            numTextViewList.forEach {
                it.isVisible = false
            }
            didRun = false
            numberPicker.value = 1
        }
    }

    private fun initAddButton() {
        addBtn.setOnClickListener {
            when {
                didRun -> showToast("초기화 후에 시도해주세요.")
                pickNumberSet.size >= 5 -> showToast("숫자는 최대 5개까지 선택할 수 있습니다.")
                pickNumberSet.contains(numberPicker.value) -> showToast("이미 선택된 숫자입니다.")
                else -> {
                    val textView = numTextViewList[pickNumberSet.size]
                    textView.isVisible = true
                    textView.text = numberPicker.value.toString()
                    setNumBack(numberPicker.value, textView)
                    pickNumberSet.add(numberPicker.value)
                }
            }
        }
    }

    // 숫자에 맞는 배경색을 지정해주는 함수
    private fun setNumBack(number: Int, textView: TextView) {
        val background = when (number) {
            in 1..10 -> R.drawable.circle_yellow
            in 11..20 -> R.drawable.circle_blue
            in 21..30 -> R.drawable.circle_red
            in 31..40 -> R.drawable.circle_grey
            else -> R.drawable.circle_green
        }

        textView.background = ContextCompat.getDrawable(this, background)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun initRunButton() {
        autoStartBtn.setOnClickListener {
            val list = getRandom()

            didRun = true

            // 랜덤으로 생성된 숫자 리스트를 순회하면서 화면에 띄우기
            list.forEachIndexed { index, number ->
                val textView = numTextViewList[index]
                textView.text = number.toString()
                textView.isVisible = true
                setNumBack(number, textView)
            }
        }
    }

    private fun getRandom(): List<Int> {
        // 1 ~ 45 중에서 pickNumberSet에 없는 숫자들만 걸러내기
        val numbers = (1..45).filter { it !in pickNumberSet }
        // pickNumberSet에 numbers를 섞고 6자리 중에 남은 자리만큼만 가져와서 오름차순 정렬하기
        return (pickNumberSet + numbers.shuffled().take(6 - pickNumberSet.size)).sorted()
    }
}