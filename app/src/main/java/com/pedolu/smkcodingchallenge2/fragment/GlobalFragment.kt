package com.pedolu.smkcodingchallenge2.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.pedolu.smkcodingchallenge2.R
import com.pedolu.smkcodingchallenge2.data.httpClient
import com.pedolu.smkcodingchallenge2.data.mathdroidApiRequest
import com.pedolu.smkcodingchallenge2.data.model.GlobalSummary
import com.pedolu.smkcodingchallenge2.data.service.CovidMathdroidService
import com.pedolu.smkcodingchallenge2.util.dismissLoading
import com.pedolu.smkcodingchallenge2.util.showLoading
import com.pedolu.smkcodingchallenge2.util.tampilToast
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.fragment_global.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class GlobalFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_global, container, false)
    }

    override fun onViewCreated(view: View, @Nullable savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        callGlobalSummaryService()
        swipeRefreshLayout.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            callGlobalSummaryService()
        })
    }

    private fun setVisible() {
        graphCard.visibility = View.VISIBLE
        confirmedCard.visibility = View.VISIBLE
        recoveredCard.visibility = View.VISIBLE
        deathCard.visibility = View.VISIBLE
        progressBar.visibility = View.GONE
    }

    private fun setInvisible() {
        graphCard.visibility = View.GONE
        confirmedCard.visibility = View.GONE
        recoveredCard.visibility = View.GONE
        deathCard.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
    }

    private fun callGlobalSummaryService() {
        setInvisible()
        showLoading(context!!, swipeRefreshLayout)
        val httpClient = httpClient()
        val mathdroidApiRequest = mathdroidApiRequest<CovidMathdroidService>(httpClient)
        val call = mathdroidApiRequest.getGlobal()
        call.enqueue(object : Callback<GlobalSummary> {
            override fun onFailure(call: Call<GlobalSummary>, t: Throwable) {
                setVisible()
                dismissLoading(swipeRefreshLayout)
            }
            override fun onResponse(
                call: Call<GlobalSummary>, response:
                Response<GlobalSummary>
            ) {
                dismissLoading(swipeRefreshLayout)
                setVisible()
                when {
                    response.isSuccessful ->
                        when {
                            response.body() != null ->
                                showGlobalSummary(response.body()!!)
                            else ->
                                tampilToast(context!!, "Berhasil")
                        }
                    else ->
                        tampilToast(context!!, "Gagal")
                }
            }
        })
    }

    fun showGlobalSummary(item: GlobalSummary) {
        createGlobalSummaryChart(item)
        txtConfirmed.text = item.confirmed.value.toString()
        txtRecovered.text = item.recovered.value.toString()
        txtDeath.text = item.deaths.value.toString()
    }

    private fun createGlobalSummaryChart(item: GlobalSummary) {
        val pieChart: PieChart = globalSummaryChart
        val listPie = ArrayList<PieEntry>()
        val listColors = ArrayList<Int>()
        listPie.add(PieEntry(item.recovered.value.toFloat()))
        listColors.add(resources.getColor(R.color.colorGreen))
        listPie.add(PieEntry(item.confirmed.value.toFloat()))
        listColors.add(resources.getColor(R.color.colorYellow))
        listPie.add(PieEntry(item.deaths.value.toFloat()))
        listColors.add(resources.getColor(R.color.colorRed))

        val pieDataSet = PieDataSet(listPie, "")
        pieDataSet.colors = listColors
        pieDataSet.setDrawValues(false)
        val pieData = PieData(pieDataSet)
        pieChart.data = pieData
        pieChart.apply {
            description.isEnabled = false
            legend.isEnabled = false
            setDrawEntryLabels(false)
            isDrawHoleEnabled = true
            holeRadius = 60f
            description.isEnabled = false
            transparentCircleRadius = 0f
            animateY(1400, Easing.EaseInOutQuad)
            setHoleColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.colorPrimary
                )
            )
            invalidate()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        this.clearFindViewByIdCache()
    }
}