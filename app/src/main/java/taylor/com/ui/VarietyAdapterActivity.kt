package taylor.com.ui

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.*
import taylor.com.adapterproxy.*
import taylor.com.layout_dsl.background_color
import taylor.com.varietyadapter.VarietyAdapter
import test.taylor.com.taylorcode.ui.recyclerview.variety.*

class VarietyAdapterActivity : AppCompatActivity(), CoroutineScope by MainScope() {
    private var itemNumber: Int = 1

    private var rv: RecyclerView? = null

    private val varietyAdapter = VarietyAdapter().apply {
        addProxy(TextProxy1())
        addProxy(TextProxy2())
        addProxy(ImageProxy())
        addProxy(EmptyProxy())
        addProxy(FooterProxy())

        // add default content for RecyclerView
        dataList = listOf(
            Text(itemNumber, "item ${itemNumber++}", 1),
//            Image("#00ff00"),
            Text(itemNumber, "item ${itemNumber++}", 1),
            Text(itemNumber, "item ${itemNumber++}", 1),
            Text(itemNumber, "item ${itemNumber++}", 1),
            Text(itemNumber, "item ${itemNumber++}", 1),
            Text(itemNumber, "item ${itemNumber++}", 2),
            Text(itemNumber, "item ${itemNumber++}", 2),
            Text(itemNumber, "item ${itemNumber++}", 2),
            Text(itemNumber, "item ${itemNumber++}", 2),
            Text(itemNumber, "item ${itemNumber++}", 2),
            Text(itemNumber, "item ${itemNumber++}", 2),
            Text(itemNumber, "item ${itemNumber++}", 2),
            Text(itemNumber, "item ${itemNumber++}", 2),
            Text(itemNumber, "item ${itemNumber++}", 2),
            Text(itemNumber, "item ${itemNumber++}", 2),
            Text(itemNumber, "item ${itemNumber++}", 2),
            Text(itemNumber, "item ${itemNumber++}", 2),
//            Image("#88ff00"),
//            Text(itemNumber, "item ${itemNumber++}", 1),
//            Text(itemNumber, "item ${itemNumber++}", 2),
//            Text(itemNumber, "item ${itemNumber++}", 2),
//            Image("#ffff00"),
//            Text(itemNumber, "item ${itemNumber++}", 1),
//            Text(itemNumber, "item ${itemNumber++}", 2),
//            Image("#098f00"),
//            Text(itemNumber, "item ${itemNumber++}", 1),
//            Footer("loading")
        )
        preloadItemCount = 2
        // define preload action
//        onPreload = {
//            // append new data to the tail of existing data
//            val oldList = dataList
//            // as if data comes from server
//            Log.v("ttaylor", "tag=asdf, VarietyAdapterActivity.()  onPreLoad")
//            launch(Dispatchers.IO) {
//                val newDataList = oldList.toMutableList().apply {
//                    val footer = removeAt(size - 1)
//                    addAll(
//                        listOf(
//                            Text(itemCount, "item ${itemNumber++}"),
//                            Text(itemCount, "item ${itemNumber++}"),
//                            Text(itemCount, "item ${itemNumber++}"),
//                            Text(itemCount, "item ${itemNumber++}"),
//                            Text(itemCount, "item ${itemNumber++}"),
//                            footer
//                        )
//                    )
//
//                }
//                delay(2000)
//                withContext(Dispatchers.Main) {
//                    dataList = newDataList
//                }
//            }
//        }

        onViewRecycled = {
            val item = "${(it as TextViewHolder).tvName?.text} is recycled"
            Log.e("ttaylor","${item}")
            Toast.makeText(this@VarietyAdapterActivity, item,Toast.LENGTH_SHORT).show()
        }

        onViewDetachedFromWindow = {
            val viewHolder = (it as? TextViewHolder2)
            Log.v("ttaylor", "tag=, onViewDetachedFromWindow.()  text=${viewHolder?.tvName?.text}")
        }

        onViewAttachedToWindow = {
            val viewHolder = (it as? TextViewHolder2)
            Log.v("ttaylor", "tag=, onViewAttachedToWindow.()  text=${viewHolder?.tvName?.text}")
        }
    }

    private val rootView by lazy {
        ConstraintLayout {
            layout_width = match_parent
            layout_height = match_parent

            Button {
                layout_id = "tvEmpty"
                layout_width = wrap_content
                layout_height = wrap_content
                textSize = 13f
                padding = 5
                textColor = "#3F4658"
                gravity = gravity_center
                text = "empty list"
                top_toTopOf = parent_id
                start_toStartOf = parent_id
                onClick = {
                    // demonstrate how to show empty view in VarietyAdapter
                    varietyAdapter.apply {
                        dataList = listOf(
                            EmptyBean("No items in the list!")
                        )
                    }
                }
            }

            Button {
                layout_id = "btnRemove"
                layout_width = wrap_content
                layout_height = wrap_content
                textSize = 13f
                padding = 5
                textColor = "#3F4658"
                gravity = gravity_center
                text = "remove"
                start_toEndOf = "tvEmpty"
                align_vertical_to = "tvEmpty"
                onClick = {
                    val newList = varietyAdapter.dataList.toMutableList().apply { removeAt(1) }
                    varietyAdapter.dataList = newList
                }
            }

            Button {
                layout_id = "btnPartUpdate"
                layout_width = wrap_content
                layout_height = wrap_content
                textSize = 13f
                padding = 5
                gravity = gravity_center
                text = "partially update"
                start_toEndOf = "btnRemove"
                align_vertical_to = "btnRemove"
                onClick = {
                    val oldData = varietyAdapter.dataList.toMutableList()
                    val first = oldData.get(1) as? Text
                    if (first != null) {
                        val newFirst = first.copy().apply {
                            text = "100"
                        }
                        oldData.set(1, newFirst)
                        varietyAdapter.dataList = oldData
                    }
                }
            }

            Button {
                layout_id = "btnPartUpdate"
                layout_width = wrap_content
                layout_height = wrap_content
                textSize = 13f
                padding = 5
                gravity = gravity_center
                text = "add item"
                start_toEndOf = "btnRemove"
                align_vertical_to = "btnRemove"
                onClick = {
                    val oldData = varietyAdapter.dataList.toMutableList()
                    val firstText = oldData.first()
                    val nextTexts = oldData.subList(1,oldData.size-1)
                    val newText = Text(itemNumber,"item insert",2)
                    val newList = mutableListOf<Any>()
                    newList.add(firstText)
                    newList.add(newText)
                    newList.addAll(nextTexts)
                    varietyAdapter.dataList = newList
                }
            }

            rv = RecyclerView {
                layout_width = 200
                layout_height = 120
                top_toBottomOf = "tvEmpty"
                bottom_toBottomOf = parent_id
                center_horizontal = true
                background_color = "#c8c8c8"
                adapter = varietyAdapter
                layoutManager = LinearLayoutManager(this@VarietyAdapterActivity)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(rootView)
    }
}

fun RecyclerView.addTopBottomListener(onBorder: (direction: Int) -> Unit) {
    addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            if (dy != 0) {
                if (!canScrollVertically(-1)) {
                    onBorder(-1)
                } else if (!canScrollVertically(1)) {
                    onBorder(1)
                }
            }
        }
    })
}