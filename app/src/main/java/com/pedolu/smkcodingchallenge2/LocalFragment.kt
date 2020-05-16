package com.pedolu.smkcodingchallenge2

import android.graphics.PorterDuff
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.pedolu.smkcodingchallenge2.data.dao.CovidMathdroidService
import com.pedolu.smkcodingchallenge2.data.httpClient
import com.pedolu.smkcodingchallenge2.data.mathdroidApiRequest
import com.pedolu.smkcodingchallenge2.data.model.Countries
import com.pedolu.smkcodingchallenge2.data.model.CountrySummary
import com.pedolu.smkcodingchallenge2.util.tampilToast
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.fragment_local.*
import kotlinx.android.synthetic.main.fragment_local.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class LocalFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_local, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        callCountries(view)
    }

    private fun setVisible(view: View) {
        view.graphCard.visibility = View.VISIBLE
        view.confirmedCard.visibility = View.VISIBLE
        view.recoveredCard.visibility = View.VISIBLE
        view.deathCard.visibility = View.VISIBLE
        view.progressBar.visibility = View.GONE
    }

    private fun setInvisible(view: View) {
        view.graphCard.visibility = View.GONE
        view.confirmedCard.visibility = View.GONE
        view.recoveredCard.visibility = View.GONE
        view.deathCard.visibility = View.GONE
        view.progressBar.visibility = View.VISIBLE
    }

    private fun callCountries(view: View) {
        setInvisible(view)
        val httpClient = httpClient()
        val mathdroidApiRequest = mathdroidApiRequest<CovidMathdroidService>(httpClient)
        val call = mathdroidApiRequest.getCountries()
        call.enqueue(object : Callback<Countries> {
            override fun onFailure(call: Call<Countries>, t: Throwable) {
                setVisible(view)
            }
            override fun onResponse(
                call: Call<Countries>, response:
                Response<Countries>
            ) {
                setVisible(view)
                when {
                    response.isSuccessful ->
                        when {
                            response.body() != null -> {
                                setCountrySpinner(response.body()!!)
                            }
                            else -> {
                                tampilToast(context!!, "Berhasil")
                            }
                        }
                    else -> {
                        tampilToast(context!!, "gagal")
                    }
                }
            }
        })
    }

    private fun callCountrySummary(country: String) {
        progressBar?.visibility = View.VISIBLE
        val httpClient = httpClient()
        val mathdroidApiRequest = mathdroidApiRequest<CovidMathdroidService>(httpClient)
        val call = mathdroidApiRequest.getCountry(country)
        call.enqueue(object : Callback<CountrySummary> {
            override fun onFailure(call: Call<CountrySummary>, t: Throwable) {
                progressBar?.visibility = View.GONE
            }

            override fun onResponse(
                call: Call<CountrySummary>, response:
                Response<CountrySummary>
            ) {
                progressBar?.visibility = View.GONE
                when {
                    response.isSuccessful ->
                        when {
                            response.body() != null ->
                                showLocalSummary(response.body()!!)
                            else ->
                                tampilToast(context!!, "Berhasil")
                        }
                    else ->
                        tampilToast(context!!, "Gagal")
                }
            }
        })
    }

    fun showLocalSummary(item: CountrySummary) {
        createLocalSummaryChart(item)
        txtConfirmed.text = item.confirmed.value.toString()
        txtRecovered.text = item.recovered.value.toString()
        txtDeath.text = item.deaths.value.toString()
    }

    private fun createLocalSummaryChart(item: CountrySummary) {
        val pieChart: PieChart = localSummaryChart
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
            setHoleColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
            invalidate()
        }
    }

    private fun setCountrySpinner(countries: Countries) {
        val item = ArrayList<String>()
        countries.countries.map { country -> item.add(country.name) }
        val adapter = ArrayAdapter(
            this.activity!!,
            android.R.layout.simple_spinner_item,
            item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        countrySpinner.background.setColorFilter(
            resources.getColor(R.color.colorWhite),
            PorterDuff.Mode.SRC_ATOP
        )
        countrySpinner.adapter = adapter
        countrySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                (parent!!.getChildAt(0) as TextView).textSize = 32f
                (parent.getChildAt(0) as TextView).gravity = Gravity.CENTER

                callCountrySummary(countrySpinner.selectedItem.toString())
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        this.clearFindViewByIdCache()
    }
}