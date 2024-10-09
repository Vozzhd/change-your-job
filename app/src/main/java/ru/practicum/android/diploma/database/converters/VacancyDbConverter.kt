package ru.practicum.android.diploma.database.converters

import ru.practicum.android.diploma.database.entities.VacancyDetailsEntity
import ru.practicum.android.diploma.search.domain.models.VacancySearch
import ru.practicum.android.diploma.vacancy.domain.entity.Vacancy

class VacancyDbConverter {
    fun map(vacancyEntity: VacancyDetailsEntity): Vacancy {
        return Vacancy(
            id = vacancyEntity.id,
            name = vacancyEntity.name,
            salary = vacancyEntity.salary,
            companyLogo = vacancyEntity.logoLink,
            companyName = vacancyEntity.companyName,
            area = vacancyEntity.area,
            address = vacancyEntity.address,
            experience = vacancyEntity.experience,
            schedule = vacancyEntity.schedule,
            employment = vacancyEntity.employment,
            description = vacancyEntity.description,
            keySkills = vacancyEntity.keySkill,
            vacancyUrl = vacancyEntity.vacancyUrl,
            isFavorite = true
        )
    }

    fun map(vacancy: Vacancy): VacancyDetailsEntity {
        return VacancyDetailsEntity(
            id = vacancy.id,
            name = vacancy.name,
            companyName = vacancy.name,
            salary = vacancy.salary,
            area = vacancy.area,
            address = vacancy.address,
            experience = vacancy.experience,
            schedule = vacancy.schedule,
            employment = vacancy.employment,
            description = vacancy.description,
            keySkill = vacancy.keySkills,
            vacancyUrl = vacancy.vacancyUrl,
            logoLink = vacancy.companyLogo,
            timeAdd = System.currentTimeMillis()
        )
    }

    fun convertToVacancySearch(vacancy: VacancyDetailsEntity): VacancySearch {
        return VacancySearch(
            id = vacancy.id,
            name = vacancy.name,
            salary = vacancy.salary,
            address = vacancy.address ?: "",
            company = vacancy.employment ?: "",
            logo = vacancy.logoLink ?: ""
        )
    }
}
