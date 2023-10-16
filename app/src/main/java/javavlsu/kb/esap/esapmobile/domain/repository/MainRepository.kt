package javavlsu.kb.esap.esapmobile.domain.repository

import javavlsu.kb.esap.esapmobile.domain.api.MainApiService
import javavlsu.kb.esap.esapmobile.domain.util.apiRequestFlow
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val mainApiService: MainApiService,
) {
    fun getDoctor() = apiRequestFlow {
        mainApiService.getDoctor()
    }

    fun getDoctorList() = apiRequestFlow {
        mainApiService.getDoctorList()
    }
}