package com.example.rospatent.classes

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Patent(
  val common: Common = Common(),
  val biblio: Biblio = Biblio(),
  val id: String = "Unknown",
  val index: String = "Unknown",
  val dataset: String = "Unknown",
  val similarity: Double = 0.0,
  @SerialName("similarity_norm")
  val similarityNorm: Double = 0.0,
  val snippet: Snippet = Snippet(),
) {
  @Serializable
  class Common {
    @SerialName("publishingOffice")
    val publishingOffice: String = "Unknown"

    @SerialName("documentNumber")
    val documentNumber: String = "Unknown"
    val kind: String = "Unknown"

    @SerialName("publication_date")
    val publicationDate: String = "2000.01.01"

    val application: Application = Application()

    @Serializable
    class Application(
      val number: String = "00000000000",
      @SerialName("filing_date")
      val filingDate: String = "2000.01.01",
    )
  }

  @Serializable
  class Biblio(
    val ru: Ru = Ru(),
    val en: En = En(),
  ) {
    @Serializable
    class Ru(
      val inventor: List<Inventor> = emptyList(),
      val title: String = "Unknown",
      val patentee: List<Patentee> = emptyList(),
    ) {
      @Serializable
      class Inventor(
        val name: String = "Unknown",
      )

      @Serializable
      class Patentee(
        val name: String = "Unknown",
      )
    }

    @Serializable
    class En(
      val inventor: List<Inventor> = emptyList(),
      val title: String = "Unknown",
      val patentee: List<Patentee> = emptyList(),
    ) {
      @Serializable
      class Inventor(
        val name: String = "Unknown",
      )

      @Serializable
      class Patentee(
        val name: String = "Unknown",
      )
    }
  }

  @Serializable
  class Snippet(
    var title: String = "Unknown",
    var description: String = "Unknown",
    val lang: String = "Unknown",
    val inventor: String = "Unknown",
    val patentee: String = "Unknown",
    val classification: Classification = Classification(),
  ) {
    @Serializable
    class Classification(
      val ipc: String = "Unknown",
    ) {
    }
  }
}