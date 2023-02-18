package com.ayushwalker.dragdrop
// https://developer.android.com/develop/ui/views/touch-and-input/drag-drop
import android.content.ClipData
import android.content.ClipDescription
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.DragEvent
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.annotation.RequiresApi

class MainActivity : AppCompatActivity() {
    private lateinit var dragView:View
    private lateinit var llTop:LinearLayout
    private lateinit var llBottom:LinearLayout

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dragView = findViewById(R.id.dragView)
        llTop = findViewById(R.id.llTop)
        llBottom = findViewById(R.id.llBottom)

//        You set the listener for a drop target with the View object's setOnDragListener() method
        llTop.setOnDragListener(dragListener)
        llBottom.setOnDragListener(dragListener)

        dragView.setOnLongClickListener{
            val clipText = "This is our ClipData Text"
            val item=ClipData.Item(clipText)
            val mimeTypes = arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN)
            val data = ClipData(clipText, mimeTypes,item)

            val dragShadowBuilder  = View.DragShadowBuilder(it)
            it.startDragAndDrop(data, dragShadowBuilder,it,0)
//            Your application notifies the system to start a drag and drop operation by calling the startDragAndDrop() method,
//            https://developer.android.com/reference/android/view/View#startDragAndDrop(android.content.ClipData,%20android.view.View.DragShadowBuilder,%20java.lang.Object,%20int)

            it.visibility = View.INVISIBLE
            true
        }
    }

//    create a drag event listener by implementing View.OnDragListener.
    private val dragListener = View.OnDragListener{ view, event ->
        when(event.action){
            DragEvent.ACTION_DRAG_STARTED ->  {
                event.clipDescription.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)
            }
            DragEvent.ACTION_DRAG_ENTERED->{
//                invalidate() means 'redraw on screen
                view.invalidate()
                true
            }
            DragEvent.ACTION_DRAG_LOCATION -> true
            DragEvent.ACTION_DRAG_EXITED -> {
                view.invalidate()
                true
            }
            DragEvent.ACTION_DROP ->{
                val item = event.clipData.getItemAt(0)
                val dragData = item.text
                Toast.makeText(this,dragData,Toast.LENGTH_SHORT).show()

                view.invalidate()

                val v = event.localState as View
                val owner = v.parent as ViewGroup
                owner.removeView(v)

                val destination = view as LinearLayout
                destination.addView(v)
                v.visibility = View.VISIBLE
                true
            }
            DragEvent.ACTION_DRAG_ENDED -> {
                view.invalidate()
                true
            }
            else -> false

        }
    }
}

/*
STEPS/NOTES:
1. Firstly change the constraint Layout into Linear Layout and insert one more Linear Layout inside the Parent Linear Layout.
2. Now add another Linear Layout at the bottom, both have layout_weight='1' and gravity = center.
3. Now add a view in the first Layout, using <View>
 */