package com.pedolu.smkcodingchallenge2.fragment

import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
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
import com.pedolu.smkcodingchallenge2.data.model.local.Countries
import com.pedolu.smkcodingchallenge2.data.model.local.CountrySummary
import com.pedolu.smkcodingchallenge2.data.model.room.CountriesModel
import com.pedolu.smkcodingchallenge2.data.model.room.LocalSummaryModel
import com.pedolu.smkcodingchallenge2.data.service.CovidMathdroidService
import com.pedolu.smkcodingchallenge2.util.dismissLoading
import com.pedolu.smkcodingchallenge2.util.showLoading
import com.pedolu.smkcodingchallenge2.util.tampilToast
import com.pedolu.smkcodingchallenge2.viewmodel.CountriesViewModel
import com.pedolu.smkcodingchallenge2.viewmodel.LocalSummaryViewModel
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.fragment_local.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class LocalFragment : Fragment() {
    private lateinit var confirmed: String
    private lateinit var recovered: String
    private lateinit var deaths: String
    private var countriesItem: ArrayList<String> = ArrayList()
    private val localSummaryViewModel by viewModels<LocalSummaryViewModel>()
    private val countriesViewModel by viewModels<CountriesViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_local, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        retrieveRoomCountries()
        callCountries()
        swipeRefreshLayout.setOnRefreshListener({
            retrieveRoomCountrySummary(countrySpinner.selectedItem.toString())
            callCountrySummary(countrySpinner.selectedItem.toString())
            showLocalSummary()
        })
    }

    private fun retrieveRoomCountries() {
        countriesViewModel.init(requireContext())
        countriesViewModel.countries.observe(viewLifecycleOwner, Observer { countries ->
            if (countries != null) {
                for (country in countries) {
                    countriesItem.add(country.name)
                }
                setCountrySpinner()
            }
        })

    }

    private fun retrieveRoomCountrySummary(country: String) {
        localSummaryViewModel.init(requireContext(), country)
        localSummaryViewModel.localSummary.observe(viewLifecycleOwner, Observer { localSummary ->
            if (localSummary != null) {
                confirmed = localSummary.confirmed
                recovered = localSummary.recovered
                deaths = localSummary.deaths
                setLastUpdate(localSummary.last_update)
                showLocalSummary()
            }
        })
    }

    private fun callCountries() {
        setInvisible()
        val httpClient = httpClient()
        val mathdroidApiRequest = mathdroidApiRequest<CovidMathdroidService>(httpClient)
        val call = mathdroidApiRequest.getCountries()
        call.enqueue(object : Callback<Countries> {
            override fun onFailure(call: Call<Countries>, t: Throwable) {
                setVisible()
            }

            override fun onResponse(
                call: Call<Countries>, response:
                Response<Countries>
            ) {
                setVisible()
                when {
                    response.isSuccessful ->
                        when {
                            response.body() != null -> {
                                countriesViewModel.init(requireContext())
                                var countries: MutableList<CountriesModel> = ArrayList()
                                for (country in response.body()!!.countries) {
                                    countriesItem.add(country.name)
                                    countries.add(CountriesModel(country.name))
                                }
                                countriesViewModel.addAllData(countries)
                                setCountrySpinner()
                            }
                            else -> {
                                tampilToast(context!!, "Berhasil")
                            }
                        }
                    else -> {
                        tampilToast(requireContext(), "Coba Lagi!")
                    }
                }
            }
        })
    }

    private fun callCountrySummary(country: String) {
        showLoading(requireContext(), swipeRefreshLayout)
        val httpClient = httpClient()
        val mathdroidApiRequest = mathdroidApiRequest<CovidMathdroidService>(httpClient)
        val call = mathdroidApiRequest.getCountry(country)
        call.enqueue(object : Callback<CountrySummary> {
            override fun onFailure(call: Call<CountrySummary>, t: Throwable) {
                dismissLoading(swipeRefreshLayout)
            }

            override fun onResponse(
                call: Call<CountrySummary>, response:
                Response<CountrySummary>
            ) {
                dismissLoading(swipeRefreshLayout)
                when {
                    response.isSuccessful ->
                        when {
                            response.body() != null -> {
                                val item = response.body()!!
                                confirmed = item.confirmed.value.toString()
                                recovered = item.recovered.value.toString()
                                deaths = item.deaths.value.toString()
                                val localSummary = LocalSummaryModel(
                                    confirmed,
                                    recovered,
                                    deaths,
                                    item.lastUpdate,
                                    country
                                )
                                localSummaryViewModel.addData(localSummary)

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

    fun showLocalSummary() {
        createLocalSummaryChart()
        txtConfirmed.text = confirmed
        txtRecovered.text = recovered
        txtDeath.text = deaths
    }

    private fun createLocalSummaryChart() {
        val pieChart: PieChart = localSummaryChart
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

    private fun setLastUpdate(lastUpdate: String) {
        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
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

    private fun setCountrySpinner() {
        val adapter = ArrayAdapter(
            this.requireActivity(),
            android.R.layout.simple_spinner_item,
            countriesItem
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
                retrieveRoomCountrySummary(countrySpinner.selectedItem.toString())
                callCountrySummary(countrySpinner.selectedItem.toString())
            }
        }
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

    override fun onDestroy() {
        super.onDestroy()
        this.clearFindViewByIdCache()
    }
}