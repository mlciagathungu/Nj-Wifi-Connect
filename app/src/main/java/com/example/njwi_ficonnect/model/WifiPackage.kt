
// ===== 6. WifiPackage.kt =====
package com.example.njwi_ficonnect.model

import java.text.SimpleDateFormat
import java.util.*

enum class PackageCategory(val displayName: String) {
    BASIC("Basic"),
    PREMIUM("Premium"),
    BUSINESS("Business"),
    FAMILY("Family"),
    STUDENT("Student"),
    UNLIMITED("Unlimited")
}

enum class DataUnit(val multiplier: Long, val suffix: String) {
    MB(1024 * 1024, "MB"),
    GB(1024 * 1024 * 1024, "GB"),
    TB(1024L * 1024 * 1024 * 1024, "TB")
}

data class WifiPackage(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val price: Double = 0.0,
    val originalPrice: Double = 0.0, // For showing discounts
    val duration: String = "", // e.g., "30 days", "1 month"
    val durationInDays: Int = 0, // Duration in days for calculations
    val dataAllowance: String = "", // e.g., "10 GB", "Unlimited"
    val dataAllowanceInBytes: Long = 0L, // For comparisons
    val speed: String = "", // e.g., "100 Mbps", "1 Gbps"
    val hasHighSpeed: Boolean = false,
    val hasNoSetupFee: Boolean = false,
    val hasSecureConnection: Boolean = false,
    val has24_7Support: Boolean = false,
    val hasUnlimitedData: Boolean = false,
    val hasFiberConnection: Boolean = false,
    val hasWifiHotspot: Boolean = false,
    val maxDevices: Int = 1, // Maximum number of connected devices
    val isMostPopular: Boolean = false,
    val isRecommended: Boolean = false,
    val isAvailable: Boolean = true,
    val category: PackageCategory = PackageCategory.BASIC,
    val categoryString: String = "", // For backward compatibility
    val features: List<String> = emptyList(), // Additional features
    val restrictions: List<String> = emptyList(), // Any limitations
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) {

    // Computed properties
    fun getFormattedPrice(): String {
        return "KSh ${String.format("%.2f", price)}"
    }

    fun getFormattedOriginalPrice(): String {
        return if (originalPrice > 0 && originalPrice != price) {
            "KSh ${String.format("%.2f", originalPrice)}"
        } else ""
    }

    fun getDiscountPercentage(): Int {
        return if (originalPrice > price && originalPrice > 0) {
            (((originalPrice - price) / originalPrice) * 100).toInt()
        } else 0
    }

    fun hasDiscount(): Boolean = getDiscountPercentage() > 0

    fun getPricePerDay(): Double {
        return if (durationInDays > 0) price / durationInDays else 0.0
    }

    fun getFormattedPricePerDay(): String {
        return "KSh ${String.format("%.2f", getPricePerDay())}/day"
    }

    fun isUnlimitedData(): Boolean = hasUnlimitedData || dataAllowance.contains("unlimited", ignoreCase = true)

    fun getDataAllowanceFormatted(): String {
        return if (isUnlimitedData()) "Unlimited" else dataAllowance
    }

    fun getSpeedFormatted(): String {
        return if (speed.isNotBlank()) "Up to $speed" else "Standard Speed"
    }

    fun getAllFeatures(): List<String> {
        val allFeatures = mutableListOf<String>()

        // Add standard features
        if (hasHighSpeed) allFeatures.add("High-Speed Internet")
        if (hasNoSetupFee) allFeatures.add("No Setup Fee")
        if (hasSecureConnection) allFeatures.add("Secure Connection")
        if (has24_7Support) allFeatures.add("24/7 Customer Support")
        if (hasUnlimitedData) allFeatures.add("Unlimited Data")
        if (hasFiberConnection) allFeatures.add("Fiber Connection")
        if (hasWifiHotspot) allFeatures.add("WiFi Hotspot")

        if (maxDevices > 1) {
            allFeatures.add("Up to $maxDevices devices")
        }

        // Add custom features
        allFeatures.addAll(features)

        return allFeatures.distinct()
    }

    fun getPopularityBadge(): String? {
        return when {
            isMostPopular -> "Most Popular"
            isRecommended -> "Recommended"
            hasDiscount() -> "${getDiscountPercentage()}% OFF"
            else -> null
        }
    }

    fun isValidPackage(): Boolean {
        return name.isNotBlank() &&
                price > 0 &&
                duration.isNotBlank() &&
                dataAllowance.isNotBlank()
    }

    fun getDurationInMillis(): Long {
        return durationInDays * 24 * 60 * 60 * 1000L
    }

    fun getExpiryDate(purchaseDate: Long = System.currentTimeMillis()): Long {
        return purchaseDate + getDurationInMillis()
    }

    fun getFormattedCreatedDate(): String {
        val sdf = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        return sdf.format(Date(createdAt))
    }

    // Comparison methods
    fun isBetterThan(other: WifiPackage): Boolean {
        val thisValueScore = calculateValueScore()
        val otherValueScore = other.calculateValueScore()
        return thisValueScore > otherValueScore
    }

    internal fun calculateValueScore(): Double {
        var score = 0.0

        // Price efficiency (lower is better)
        score += if (durationInDays > 0) (1000.0 / getPricePerDay()) else 0.0

        // Data allowance (more is better)
        score += if (isUnlimitedData()) 1000.0 else (dataAllowanceInBytes / 1_000_000.0) // MB

        // Features bonus
        score += getAllFeatures().size * 10.0

        // Speed bonus
        if (hasHighSpeed) score += 50.0
        if (hasFiberConnection) score += 100.0

        return score
    }

    companion object {
        fun createBasicPackage(
            name: String,
            price: Double,
            durationInDays: Int,
            dataAllowance: String
        ): WifiPackage {
            return WifiPackage(
                id = generatePackageId(),
                name = name,
                price = price,
                durationInDays = durationInDays,
                duration = "${durationInDays} days",
                dataAllowance = dataAllowance,
                category = PackageCategory.BASIC,
                hasSecureConnection = true,
                has24_7Support = true
            )
        }

        fun createUnlimitedPackage(
            name: String,
            price: Double,
            durationInDays: Int,
            speed: String = "100 Mbps"
        ): WifiPackage {
            return WifiPackage(
                id = generatePackageId(),
                name = name,
                price = price,
                durationInDays = durationInDays,
                duration = "${durationInDays} days",
                dataAllowance = "Unlimited",
                speed = speed,
                hasUnlimitedData = true,
                hasHighSpeed = true,
                hasFiberConnection = true,
                hasSecureConnection = true,
                has24_7Support = true,
                hasWifiHotspot = true,
                category = PackageCategory.UNLIMITED,
                maxDevices = 10
            )
        }

        private fun generatePackageId(): String {
            return "PKG_${System.currentTimeMillis()}_${(1000..9999).random()}"
        }

        fun parseDataAllowance(dataString: String): Long {
            if (dataString.contains("unlimited", ignoreCase = true)) {
                return Long.MAX_VALUE
            }

            val regex = Regex("([0-9.]+)\\s*(MB|GB|TB)", RegexOption.IGNORE_CASE)
            val matchResult = regex.find(dataString) ?: return 0L

            val amount = matchResult.groupValues[1].toDoubleOrNull() ?: return 0L
            val unit = matchResult.groupValues[2].uppercase()

            return when (unit) {
                "MB" -> (amount * DataUnit.MB.multiplier).toLong()
                "GB" -> (amount * DataUnit.GB.multiplier).toLong()
                "TB" -> (amount * DataUnit.TB.multiplier).toLong()
                else -> 0L
            }
        }

        fun formatDataSize(bytes: Long): String {
            return when {
                bytes >= DataUnit.TB.multiplier -> "${bytes / DataUnit.TB.multiplier} TB"
                bytes >= DataUnit.GB.multiplier -> "${bytes / DataUnit.GB.multiplier} GB"
                bytes >= DataUnit.MB.multiplier -> "${bytes / DataUnit.MB.multiplier} MB"
                else -> "$bytes bytes"
            }
        }
    }
}

// Extension functions for collections
fun List<WifiPackage>.getMostPopular(): WifiPackage? = find { it.isMostPopular }

fun List<WifiPackage>.getRecommended(): List<WifiPackage> = filter { it.isRecommended }

fun List<WifiPackage>.getByCategory(category: PackageCategory): List<WifiPackage> =
    filter { it.category == category }

fun List<WifiPackage>.getAvailable(): List<WifiPackage> = filter { it.isAvailable }

fun List<WifiPackage>.sortByValue(): List<WifiPackage> =
    sortedByDescending { it.calculateValueScore() }

fun List<WifiPackage>.sortByPrice(ascending: Boolean = true): List<WifiPackage> =
    if (ascending) sortedBy { it.price } else sortedByDescending { it.price }

fun List<WifiPackage>.getInPriceRange(minPrice: Double, maxPrice: Double): List<WifiPackage> =
    filter { it.price in minPrice..maxPrice }

