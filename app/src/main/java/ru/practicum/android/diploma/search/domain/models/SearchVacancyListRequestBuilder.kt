package ru.practicum.android.diploma.search.domain.models

object SearchVacancyListRequestBuilder {
    var text = ""
    const val perPage = " &per_page=20"

    private val expression = StringBuilder(text + perPage)

    fun setText(string: String) {
        text = "?text=${string}"
    }

    fun setArea(areaId: String) {
        expression.append("&area=${areaId}")
    }

    fun setIndustry(industryId: String) {
        expression.append("&industry=${industryId}")
    }

    fun setIfSalarySpecified(salarySpecified: Boolean) {
        if (salarySpecified) {
            expression.append("&only_with_salary=true")
        }
    }

    fun setSalary(salary: String) {
        expression.append("&salary=${salary}")
    }

    fun setPage(page: String) {
        expression.append("&page=${page}")
    }

    fun setHost(countryId: String) {
        val host: String = when (countryId) {
            "113" -> "hh.ru" //Россия
            "5" -> "hh.ru" //Украина
            "40" -> "hh.kz" //Казахстан
            "9" -> "hh1.az" // Азербайджан
            "16" -> "rabota.by"//Беларусь
            "28" -> "headhunter.ge"//Грузия
            "48" -> "headhunter.kg"//Кыргызстан
            "97" -> "hh.uz" //Узбекистан
            else -> "hh.ru"
        }
        expression.append("&host==${host}")
    }

    fun getExpression(): String {
        return expression.toString()
    }
}
