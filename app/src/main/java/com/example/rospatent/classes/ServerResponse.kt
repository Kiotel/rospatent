package com.example.rospatent.classes

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RosPatentResponse(
	val total: Int = 0,
	val available: Int = 0,
	@SerialName("hits")
	val patents: List<Patent>,
)