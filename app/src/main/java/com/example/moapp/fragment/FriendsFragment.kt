package com.example.moapp.fragment

import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moapp.databinding.FragmentChatBinding
import com.example.moapp.databinding.ItemChatBinding
import com.example.moapp.databinding.ItemFriendsBinding
import com.example.moapp.model.ChatModel
import com.example.moapp.model.User
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.BufferedReader
import java.io.InputStreamReader

class FriendsViewHolder(val binding: ItemFriendsBinding) :
    RecyclerView.ViewHolder(binding.root)
class FriendsAdapter(val userModels: List<User>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    // 항목의 개수를 판단하기 위해 자동 호출
    override fun getItemCount(): Int {
        return userModels.size
    }

    // 항목 뷰를 가지는 뷰 홀더를 준비하기 위해 자동 호출
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            RecyclerView.ViewHolder =
        FriendsViewHolder(
            ItemFriendsBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )

    // RecyclerViewAdapter 내의 onBindViewHolder 메서드에서의 코드 일부분
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        // 여러 사용자에 대한 처리
        val binding = (holder as FriendsViewHolder).binding

        // 데이터 바인딩 및 처리 코드 추가
        binding.friendsItemTextviewId.id = userModels[position].id
        binding.friendsTextviewTitle.text = userModels[position].name
        Glide.with(binding.friendsItemImageview.context)
            .load(userModels[position].img)
            .into(binding.friendsItemImageview)
    }
}

class FriendsDecoration(val context: Context): RecyclerView.ItemDecoration() {
    // 각 항목을 꾸미기 위해 호출
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.set(10, 10, 10, 0)
        view.setBackgroundColor(Color.parseColor("#EEE8B7"))
        ViewCompat.setElevation(view, 20.0f)
    }
}

class FriendsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentChatBinding.inflate(inflater, container, false)

        // JSON 파일에서 데이터 로드
        val json = loadJsonFromAsset(requireContext(), "friends_data.json")

        // User 객체로 변환
        val userList: List<User> = Gson().fromJson(json, object : TypeToken<List<User>>() {}.type)

        // 리사이클러 뷰에 LayoutManager, Adapter 적용
        val layoutManager = LinearLayoutManager(activity)
        binding.recyclerView.layoutManager = layoutManager
        val adapter = FriendsAdapter(userList)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.addItemDecoration(FriendsDecoration(activity as Context))
        return binding.root
    }

    private fun loadJsonFromAsset(context: Context, fileName: String): String? {
        return try {
            val inputStream = context.assets.open(fileName)
            val bufferedReader = BufferedReader(InputStreamReader(inputStream))
            bufferedReader.use { it.readText() }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
