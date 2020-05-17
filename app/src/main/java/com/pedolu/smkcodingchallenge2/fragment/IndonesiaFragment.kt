package com.pedolu.smkcodingchallenge2.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.pedolu.smkcodingchallenge2.R
import com.pedolu.smkcodingchallenge2.adapter.IndonesiaAdapter
import com.pedolu.smkcodingchallenge2.data.httpClient
import com.pedolu.smkcodingchallenge2.data.kawalCoronaApiRequest
import com.pedolu.smkcodingchallenge2.data.model.ProvinsiItem
import com.pedolu.smkcodingchallenge2.data.service.CovidKawalCoronaService
import com.pedolu.smkcodingchallenge2.util.dismissLoading
import com.pedolu.smkcodingchallenge2.util.showLoading
import com.pedolu.smkcodingchallenge2.util.tampilToast
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.fragment_indonesia.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class IndonesiaFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_indonesia, container, false)
    }

    override fun onViewCreated(view: View, @Nullable savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        callIndonesiaProvinsi()
        swipeRefreshLayout.post { callIndonesiaProvinsi() }
        swipeRefreshLayout.setOnRefreshListener {
            callIndonesiaProvinsi()
        }
    }

    private fun setVisible() {
        txtIndonesia.visibility = View.VISIBLE
        listProvinsiIndonesia.visibility = View.VISIBLE
        progressBar.visibility = View.GONE
    }

    private fun setInvisible() {
        txtIndonesia.visibility = View.GONE
        listProvinsiIndonesia.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
    }

    private fun callIndonesiaProvinsi() {
        setInvisible()
        showLoading(context!!, swipeRefreshLayout)
        val httpClient = httpClient()
        val apiRequest = kawalCoronaApiRequest<CovidKawalCoronaService>(httpClient)
        val call = apiRequest.getProvinsi()
        call.enqueue(object : Callback<List<ProvinsiItem>> {
            override fun onFailure(call: Call<List<ProvinsiItem>>, t: Throwable) {
                tampilToast(context!!, "Gagal")
                dismissLoading(swipeRefreshLayout)
                setVisible()
            }

            override fun onResponse(
                call: Call<List<ProvinsiItem>>, response:
                Response<List<ProvinsiItem>>
            ) {
                setVisible()
                dismissLoading(swipeRefreshLayout)
                when {
                    response.isSuccessful -> {
                        when {
                            response.body()?.size != 0 ->
                                showIndonesiaSummary(response.body()!!)
                            else -> {
                                tampilToast(context!!, "Berhasil")
                            }
                        }
                    }
                    else -> {
                        tampilToast(context!!, "Gagal")
                    }
                }
            }
        })
    }

    private fun showIndonesiaSummary(provinsi: List<ProvinsiItem>) {
        listProvinsiIndonesia.layoutManager = LinearLayoutManager(context)
        listProvinsiIndonesia.adapter =
            IndonesiaAdapter(
                context!!,
                provinsi
            ) {
                val provinsi = it
                tampilToast(context!!, provinsi.attributes.provinsi)
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        this.clearFindViewByIdCache()
    }
}
