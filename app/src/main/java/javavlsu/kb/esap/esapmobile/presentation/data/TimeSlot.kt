package javavlsu.kb.esap.esapmobile.presentation.data

import javavlsu.kb.esap.esapmobile.domain.model.response.AppointmentResponse
import javavlsu.kb.esap.esapmobile.domain.model.response.ScheduleResponse
import java.time.LocalTime
import java.time.format.DateTimeFormatter

data class TimeSlot(
    val startTime: LocalTime,
    val endTime: LocalTime
)

fun calculateAvailableTimeSlots(
    doctorSchedules: List<ScheduleResponse>,
    doctorAppointments: List<AppointmentResponse>
): List<TimeSlot> {
    val timeSlots = mutableListOf<TimeSlot>()
    val intervalDuration = 30
    val formatter = DateTimeFormatter.ofPattern("HH:mm:ss")

    for (schedule in doctorSchedules) {
        val startTime = LocalTime.parse(schedule.startDoctorAppointment, formatter)
        val endTime = LocalTime.parse(schedule.endDoctorAppointment, formatter)

        var currentTime = startTime
        while (currentTime.plusMinutes(intervalDuration.toLong()) <= endTime) {
            val timeSlot = TimeSlot(
                startTime = currentTime,
                endTime = currentTime.plusMinutes(intervalDuration.toLong())
            )
            currentTime = currentTime.plusMinutes(intervalDuration.toLong())

            if (!isTimeSlotBooked(timeSlot, doctorAppointments)) {
                timeSlots.add(timeSlot)
            }
        }
    }

    return timeSlots
}

fun isTimeSlotBooked(timeSlot: TimeSlot, doctorAppointments: List<AppointmentResponse>): Boolean {
    for (appointment in doctorAppointments) {
        val appointmentStartTime = LocalTime.parse(appointment.startAppointments)
        val appointmentEndTime = LocalTime.parse(appointment.endAppointments)

        if (appointmentStartTime < timeSlot.endTime && appointmentEndTime > timeSlot.startTime) {
            return true
        }
    }
    return false
}