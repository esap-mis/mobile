package javavlsu.kb.esap.esapmobile.core.domain.network

import com.russhwolf.settings.Settings
import java.io.File
import java.util.Properties

class AutoSaveFilePropertiesSettings(
    private val properties: Properties,
    private val propertiesFile: File,
) : Settings {

    override val keys: Set<String>
        get() = properties.keys.map { it.toString() }.toSet()

    override val size: Int
        get() = properties.size

    override fun putString(key: String, value: String) {
        properties[key] = value
        saveToFile()
    }

    override fun getString(key: String, defaultValue: String): String {
        return properties[key]?.toString() ?: defaultValue
    }

    override fun getStringOrNull(key: String): String? {
        return properties[key]?.toString()
    }

    override fun clear() {
        properties.clear()
        saveToFile()
    }

    override fun remove(key: String) {
        properties.remove(key)
        saveToFile()
    }

    override fun hasKey(key: String): Boolean {
        return properties.containsKey(key)
    }

    override fun putLong(key: String, value: Long) {
        properties[key] = value.toString()
        saveToFile()
    }

    override fun getLong(key: String, defaultValue: Long): Long {
        return properties[key]?.toString()?.toLongOrNull() ?: defaultValue
    }

    override fun getLongOrNull(key: String): Long? {
        return properties[key]?.toString()?.toLongOrNull()
    }

    override fun putInt(key: String, value: Int) {
        properties[key] = value.toString()
        saveToFile()
    }

    override fun getInt(key: String, defaultValue: Int): Int {
        return properties[key]?.toString()?.toIntOrNull() ?: defaultValue
    }

    override fun getIntOrNull(key: String): Int? {
        return properties[key]?.toString()?.toIntOrNull()
    }

    override fun putFloat(key: String, value: Float) {
        properties[key] = value.toString()
        saveToFile()
    }

    override fun getFloat(key: String, defaultValue: Float): Float {
        return properties[key]?.toString()?.toFloatOrNull() ?: defaultValue
    }

    override fun getFloatOrNull(key: String): Float? {
        return properties[key]?.toString()?.toFloatOrNull()
    }

    override fun putDouble(key: String, value: Double) {
        properties[key] = value.toString()
        saveToFile()
    }

    override fun getDouble(key: String, defaultValue: Double): Double {
        return properties[key]?.toString()?.toDoubleOrNull() ?: defaultValue
    }

    override fun getDoubleOrNull(key: String): Double? {
        return properties[key]?.toString()?.toDoubleOrNull()
    }

    override fun putBoolean(key: String, value: Boolean) {
        properties[key] = value.toString()
        saveToFile()
    }

    override fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return properties[key]?.toString()?.toBooleanStrictOrNull() ?: defaultValue
    }

    override fun getBooleanOrNull(key: String): Boolean? {
        return properties[key]?.toString()?.toBooleanStrictOrNull()
    }

    private fun saveToFile() {
        try {
            propertiesFile.parentFile?.mkdirs()
            propertiesFile.outputStream().use { output ->
                properties.store(output, "ESAP Application Settings")
            }
        } catch (e: Exception) {
            println("Failed to save settings: ${e.message}")
        }
    }
}