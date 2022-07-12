package com.tablegraphictask.ui.graphic.view

import android.Manifest
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.*
import android.widget.GridLayout
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.android.material.snackbar.Snackbar
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.tablegraphictask.R
import com.tablegraphictask.databinding.FragmentDisplayGraphicBinding
import com.tablegraphictask.ui.points.model.Point
import com.tablegraphictask.utils.PermissionUtil
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
@AndroidEntryPoint
class DisplayGraphicFragment : Fragment(), MultiplePermissionsListener {

    private var _binding: FragmentDisplayGraphicBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var points: List<Point>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        arguments?.let {
            points = it.getSerializable(ARG_POINTS) as List<Point>
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDisplayGraphicBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lineChart.apply {
            xAxis.labelRotationAngle = 0f
            axisRight.isEnabled = false
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            setTouchEnabled(true)
            setPinchZoom(true)
            description = null
            legendRenderer
        }

        drawTable()
        drawLineChart()
    }

    private fun drawTable() {
        binding.gridLayout.rowCount = 2
        binding.gridLayout.columnCount = points.size + 1
        val newPointsList = mutableListOf<String>()
        newPointsList.add("x")
        for (point in points) {
            newPointsList.add(point.pX.toString())
        }
        newPointsList.add("y")
        for (point in points) {
            newPointsList.add(point.pY.toString())
        }

        for (oXY in newPointsList) {
            val layoutParams = GridLayout.LayoutParams(
                GridLayout.spec(GridLayout.UNDEFINED),
                GridLayout.spec(GridLayout.UNDEFINED),
            ).apply {
                width = 250
                height = 150
            }
            layoutParams.setGravity(Gravity.CENTER)
            layoutParams.setMargins(2, 2, 2, 2)
            val textView = TextView(requireContext()).apply {
                setTextColor(Color.BLACK)
                setTypeface(null, Typeface. BOLD)
                textSize = 21f
                text = oXY
                setBackgroundColor(
                    ResourcesCompat.getColor(resources, R.color.primary_black5, null)
                )
                gravity = Gravity.CENTER
            }
            binding.gridLayout.addView(textView, layoutParams)
        }
    }

    private fun drawLineChart() {
        val entries = points.map {
            Entry(it.pX, it.pY)
        }
        val dataSet = LineDataSet(entries, "Points")
        dataSet.apply {
            setDrawValues(false)
            setDrawFilled(false)
            setDrawCircleHole(false)
            lineWidth = 3f
            setCircleColor(Color.RED)
            mode = LineDataSet.Mode.LINEAR
        }

        binding.lineChart.apply {
            data = LineData(dataSet)
            invalidate()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_save -> {
                PermissionUtil.checkPermissions(
                    requireActivity(),
                    listOf(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ),
                    this
                )
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onPermissionsChecked(report: MultiplePermissionsReport) {
        val grantedResponses: List<PermissionGrantedResponse> = report.grantedPermissionResponses

        var writeExtPermission = false
        var readExtPermission = false

        for (response in grantedResponses) {
            when (response.permissionName) {
                Manifest.permission.WRITE_EXTERNAL_STORAGE -> writeExtPermission = true
                Manifest.permission.READ_EXTERNAL_STORAGE -> readExtPermission = true
            }
        }

        if (writeExtPermission && readExtPermission) {
            val isSaved = binding.lineChart.saveToGallery(
                "LineChart_" + Date().time
            )
            if (isSaved) {
                Snackbar.make(
                    requireView(),
                    getString(R.string.chart_saved),
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        } else {
            Snackbar.make(
                requireView(),
                getString(R.string.chart_no_permission),
                Snackbar.LENGTH_SHORT).show()
        }
    }

    override fun onPermissionRationaleShouldBeShown(
        p0: MutableList<PermissionRequest>?,
        token: PermissionToken?
    ) {
        token?.continuePermissionRequest()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val ARG_POINTS = "arg_points"
    }
}