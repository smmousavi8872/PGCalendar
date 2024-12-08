package com.github.smmousavi

import com.github.smmousavi.database.entity.ProductEntity
import com.github.smmousavi.database.entity.RatingEntity
import com.github.smmousavi.model.Product
import com.github.smmousavi.model.Rating
import com.github.smmousavi.network.response.ProductResponse
import com.github.smmousavi.network.response.RatingResponse

fun ProductResponse.asEntity() = ProductEntity(
    id = id ?: 0,
    title = title ?: "",
    price = price ?: 0.0,
    description = description ?: "",
    category = category ?: "",
    image = image ?: "",
    rating = rating?.asEntity() ?: RatingEntity(0.0, 0)
)

private fun RatingResponse.asEntity() = RatingEntity(
    rate = rate ?: 0.0,
    count = count ?: 0
)

fun ProductEntity.asExternalModel() = Product(
    id = id,
    title = title,
    price = price,
    description = description,
    category = category,
    image = image,
    rating = rating.asExternalModel()
)

private fun RatingEntity.asExternalModel() = Rating(
    rate = rate,
    count = count
)

fun Product.asResponse() = ProductResponse(
    id = id,
    title = title,
    price = price,
    description = description,
    category = category,
    image = image,
    rating = rating.asResponse()
)

fun Rating.asResponse() = RatingResponse(
    rate = rate,
    count = count
)