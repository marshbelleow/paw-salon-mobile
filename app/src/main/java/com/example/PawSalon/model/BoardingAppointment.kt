data class BoardingAppointment(
    val firstName: String,
    val lastName: String,
    val phone: String,
    val email: String,
    val address: String,
    val furBabyName: String,
    val petType: String,
    val additionalDetails: String? // This can remain nullable if it's optional
)
