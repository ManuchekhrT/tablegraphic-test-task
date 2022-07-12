package com.tablegraphictask.ui.points.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.libmvp.base.view.BaseMvpFragment
import com.tablegraphictask.R
import com.tablegraphictask.databinding.FragmentSelectPointsBinding
import com.tablegraphictask.ui.graphic.view.DisplayGraphicFragment
import com.tablegraphictask.ui.points.model.Point
import com.tablegraphictask.ui.points.presenter.PointsPresenter
import dagger.hilt.android.AndroidEntryPoint
import java.io.Serializable

@AndroidEntryPoint
class SelectPointsFragment : BaseMvpFragment<SelectPointsView, PointsPresenter>(),
    SelectPointsView {

    private var _binding: FragmentSelectPointsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSelectPointsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSubmit.setOnClickListener {
            val pointsCount = binding.edtEnterPointsCount.text.toString()
            if (pointsCount.isBlank()) {
                binding.edtEnterPointsCount.error =
                    resources.getString(R.string.error_entering_points_number)
                return@setOnClickListener
            }

            presenter.fetchPoints(pointsCount.toInt())
        }
    }

    override fun displayAllPoints(points: List<Point>) {
        binding.pbLoadingContainer.isVisible = false
        findNavController().navigate(
            R.id.action_SelectPointsFragment_to_DisplayGraphicFragment,
            bundleOf(DisplayGraphicFragment.ARG_POINTS to points as Serializable)
        )
    }

    override fun onError(error: Throwable) {
        binding.pbLoadingContainer.isVisible = false
        Toast.makeText(requireContext(), error.message, Toast.LENGTH_LONG).show();
    }

    override fun onProgress() {
        binding.pbLoadingContainer.isVisible = true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}