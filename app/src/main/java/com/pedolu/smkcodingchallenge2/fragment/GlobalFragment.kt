package com.pedolu.smkcodingchallenge2.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.pedolu.smkcodingchallenge2.R
import com.pedolu.smkcodingchallenge2.data.httpClient
import com.pedolu.smkcodingchallenge2.data.mathdroidApiRequest
import com.pedolu.smkcodingchallenge2.data.model.global.GlobalSummary
import com.pedolu.smkcodingchallenge2.data.model.room.GlobalSummaryModel
import com.pedolu.smkcodingchallenge2.data.service.CovidMathdroidService
import com.pedolu.smkcodingchallenge2.util.dismissLoading
import com.pedolu.smkcodingchallenge2.util.showLoading
import com.pedolu.smkcodingchallenge2.util.tampilToast
import com.pedolu.smkcodingchallenge2.viewmodel.GlobalSummaryViewModel
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.fragment_global.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class GlobalFragment : Fragment() {
    private lateinit var confirmed: String
    private lateinit var recovered: String
    private lateinit var deaths: String
    private val globalSummaryViewModel by viewModels<GlobalSummaryViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_global, container, false)
    }

    override fun onViewCreated(view: View, @Nullable savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        retriveRoomGlobalSummary()
        callGlobalSummaryService()
        swipeRefreshLayout.setOnRefreshListener {
            retriveRoomGlobalSummary()
            callGlobalSummaryService()
            showGlobalSummary()
        }
    }

    private fun retriveRoomGlobalSummary() {
        globalSummaryViewModel.init(requireContext())
        globalSummaryViewModel.globalSummary.observe(viewLifecycleOwner, Observer { globalSummary ->
            if (globalSummary != null) {
                confirmed = globalSummary.confirmed
                recovered = globalSummary.recovered
                deaths = globalSummary.deaths
                setLastUpdate(globalSummary.last_update)
                showGlobalSummary()
            }
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
        showLoading(requireContext(), swipeRefreshLayout)
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
                            response.body() != null -> {
                                val item = response.body()!!
                                confirmed = item.confirmed.value.toString()
                                recovered = item.recovered.value.toString()
                                deaths = item.deaths.value.toString()
                                val globalSummary = GlobalSummaryModel(
                                    confirmed,
                                    recovered,
                                    deaths,
                                    item.lastUpdate,
                                    item.lastUpdate
                                )
                                globalSummaryViewModel.addData(globalSummary)
                                setLastUpdate(item.lastUpdate)
                                showGlobalSummary()
                            }
                            else ->
                                tampilToast(context!!, "Berhasil")
                        }
                    else ->
                        tampilToast(context!!, "Gagal")
                }
            }
        })
    }

    @SuppressLint("SimpleDateFormat")
    private fun setLastUpdate(lastUpdate: String) {
        val format =
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        try {
            val date: Date = format.parse(lastUpdate)
            val formatter =
                SimpleDateFormat("yyyy-MM-dd | HH:mm:ss")
            val output =
                formatter.format(date)
            txtLastUpdate.text = output.toString()
        } catch (e: ParseException) {
            e.printStackTrace()
            Log.e("erno", e.message)
        }
    }

    private fun showGlobalSummary() {
        createGlobalSummaryChart()
        txtConfirmed.text = confirmed
        txtRecovered.text = recovered
        txtDeath.text = deaths
    }

    private fun createGlobalSummaryChart() {
        val pieChart: PieChart = globalSummaryChart
        val listPie = ArrayList<PieEntry>()
        val listColors = ArrayList<Int>()
        listPie.add(PieEntry(recovered.toFloat()))
        listColors.add(resources.getColor(R.color.colorGreen))
        listPie.add(PieEntry(confirmed.toFloat()))
        listColors.add(resources.getColor(R.color.colorYellow))
        listPie.add(PieEntry(deaths.toFloat()))
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