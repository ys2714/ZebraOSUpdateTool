package com.zebra.emdk_kotlin_wrapper.dw
import androidx.annotation.Keep

@Keep
object DWAPI {

    const val TAG = "DWAPI"

    @Keep
    const val MILLISECONDS_DELAY_BETWEEN_API_CALLS: Long = 200

    @Keep
    enum class StringBoolean(val value: String) {
        TRUE("true"),
        FALSE("false");

        @Deprecated("please use .string instead", ReplaceWith("string"), level = DeprecationLevel.ERROR)
        override fun toString(): String {
            throw RuntimeException("Not Implemented")
        }

        val string: String
            get() = value.toString()
    }

    @Keep
    enum class StringEnabled(val value: String) {
        ENABLED("ENABLED"),
        DISABLED("DISABLED");

        @Deprecated("please use .string instead", ReplaceWith("string"), level = DeprecationLevel.ERROR)
        override fun toString(): String {
            throw RuntimeException("Not Implemented")
        }

        val string: String
            get() = value.toString()
    }

    @Keep
    enum class ActionNames(val value: String) {
        ACTION("com.symbol.datawedge.api.ACTION");

        @Deprecated("please use .string instead", ReplaceWith("string"), level = DeprecationLevel.ERROR)
        override fun toString(): String {
            throw RuntimeException("Not Implemented")
        }

        val string: String
            get() = value.toString()
    }

    @Keep
    enum class ActionExtraKeys(val value: String) { // Renamed from Action to avoid conflict with Intent Action
        CREATE_PROFILE("com.symbol.datawedge.api.CREATE_PROFILE"),
        DELETE_PROFILE("com.symbol.datawedge.api.DELETE_PROFILE"),
        SWITCH_TO_PROFILE("com.symbol.datawedge.api.SWITCH_TO_PROFILE"),
        SWITCH_DATACAPTURE("com.symbol.datawedge.api.SWITCH_DATACAPTURE"),
        SWITCH_SCANNER_PARAMS("com.symbol.datawedge.api.SWITCH_SCANNER_PARAMS"),
        SET_CONFIG("com.symbol.datawedge.api.SET_CONFIG"),
        GET_CONFIG("com.symbol.datawedge.api.GET_CONFIG"),
        ENABLE_DATAWEDGE("com.symbol.datawedge.api.ENABLE_DATAWEDGE"),
        SCANNER_INPUT_PLUGIN("com.symbol.datawedge.api.SCANNER_INPUT_PLUGIN"),
        SOFT_SCAN_TRIGGER("com.symbol.datawedge.api.SOFT_SCAN_TRIGGER"),
        GET_DATAWEDGE_STATUS("com.symbol.datawedge.api.GET_DATAWEDGE_STATUS"),
        GET_SCANNER_STATUS("com.symbol.datawedge.api.GET_SCANNER_STATUS"),
        REGISTER_FOR_NOTIFICATION("com.symbol.datawedge.api.REGISTER_FOR_NOTIFICATION"),
        UNREGISTER_FOR_NOTIFICATION("com.symbol.datawedge.api.UNREGISTER_FOR_NOTIFICATION"),
        ENUMERATE_SCANNERS("com.symbol.datawedge.api.ENUMERATE_SCANNERS");

        @Deprecated("please use .string instead", ReplaceWith("string"), level = DeprecationLevel.ERROR)
        override fun toString(): String {
            throw RuntimeException("Not Implemented")
        }

        val string: String
            get() = value.toString()
    }

    @Keep
    enum class ResultCategoryNames(val value: String) {
        CATEGORY_DEFAULT("android.intent.category.DEFAULT");

        @Deprecated("please use .string instead", ReplaceWith("string"), level = DeprecationLevel.ERROR)
        override fun toString(): String {
            throw RuntimeException("Not Implemented")
        }

        val string: String
            get() = value.toString()
    }

    @Keep
    enum class ResultActionNames(val value: String) {
        RESULT_ACTION("com.symbol.datawedge.api.RESULT_ACTION"),
        SCAN_RESULT_ACTION("com.zebra.emdk_kotlin_wrapper.SCAN_RESULT_ACTION");

        @Deprecated("please use .string instead", ReplaceWith("string"), level = DeprecationLevel.ERROR)
        override fun toString(): String {
            throw RuntimeException("Not Implemented")
        }

        val string: String
            get() = value.toString()
    }

    @Keep
    enum class ResultExtraKeys(val value: String) {
        GET_DATAWEDGE_STATUS("com.symbol.datawedge.api.RESULT_GET_DATAWEDGE_STATUS"),
        GET_CONFIG("com.symbol.datawedge.api.RESULT_GET_CONFIG"),
        ENUMERATE_SCANNERS("com.symbol.datawedge.api.RESULT_ENUMERATE_SCANNERS"),
        SCANNER_STATUS("com.symbol.datawedge.api.RESULT_SCANNER_STATUS");

        @Deprecated("please use .string instead", ReplaceWith("string"), level = DeprecationLevel.ERROR)
        override fun toString(): String {
            throw RuntimeException("Not Implemented")
        }

        val string: String
            get() = value.toString()
    }

    @Keep
    enum class ConfigModeOptions(val value: String) {
        CREATE_IF_NOT_EXIST("CREATE_IF_NOT_EXIST"),
        OVERWRITE("OVERWRITE"),
        UPDATE("UPDATE");

        @Deprecated("please use .string instead", ReplaceWith("string"), level = DeprecationLevel.ERROR)
        override fun toString(): String {
            throw RuntimeException("Not Implemented")
        }

        val string: String
            get() = value.toString()
    }

    @Keep
    enum class ControlScannerInputPluginCommand(val value: String) {
        SUSPEND_PLUGIN("SUSPEND_PLUGIN"),
        RESUME_PLUGIN("RESUME_PLUGIN"),
        ENABLE_PLUGIN("ENABLE_PLUGIN"),
        DISABLE_PLUGIN("DISABLE_PLUGIN");

        @Deprecated("please use .string instead", ReplaceWith("string"), level = DeprecationLevel.ERROR)
        override fun toString(): String {
            throw RuntimeException("Not Implemented")
        }

        val string: String
            get() = value.toString()
    }

    @Keep
    enum class ScannerStatus(val value: String) {
        UNKNOWN("UNKNOWN"),
        IDLE("IDLE"), // Scanner is idle
        WAITING("WAITING"), // Scanner is ready to be triggered
        SCANNING("SCANNING"), // Scanner is emitting a scanner beam
        DISABLED("DISABLED"), // Scanner is disabled
        CONNECTED("CONNECTED"), // An external (Bluetooth or serial) scanner is connected
        DISCONNECTED("DISCONNECTED"); // The external scanner is disconnected

        @Deprecated("please use .string instead", ReplaceWith("string"), level = DeprecationLevel.ERROR)
        override fun toString(): String {
            throw RuntimeException("Not Implemented")
        }

        val string: String
            get() = value.toString()
    }

    @Keep
    enum class ScannerIdentifiers(val value: String) {
        AUTO("AUTO"),
        INTERNAL_IMAGER("INTERNAL_IMAGER"),
        INTERNAL_LASER("INTERNAL_LASER"),
        INTERNAL_CAMERA("INTERNAL_CAMERA"),
        SERIAL_SSI("SERIAL_SSI"),
        BLUETOOTH_SSI("BLUETOOTH_SSI"),
        BLUETOOTH_RS5100("BLUETOOTH_RS5100"),
        BLUETOOTH_RS6000("BLUETOOTH_RS6000"),
        BLUETOOTH_DS2278("BLUETOOTH_DS2278"),
        BLUETOOTH_DS3678("BLUETOOTH_DS3678"),
        BLUETOOTH_DS8178("BLUETOOTH_DS8178"),
        BLUETOOTH_LI3678("BLUETOOTH_LI3678"),
        BLUETOOTH_ZEBRA("BLUETOOTH_ZEBRA"),
        PLUGABLE_SSI("PLUGABLE_SSI"),
        PLUGABLE_SSI_RS5000("PLUGABLE_SSI_RS5000"),
        USB_SSI_DS3608("USB_SSI_DS3608"),
        USB_TGCS_MP7000("USB_TGCS_MP7000"),
        USB_ZEBRA("USB_ZEBRA"),
        USB_ZEBRACRADLE("USB_ZEBRACRADLE"),
        USB_SNAPI_ZEBRA("USB_SNAPI_ZEBRA");

        @Deprecated("please use .string instead", ReplaceWith("string"), level = DeprecationLevel.ERROR)
        override fun toString(): String {
            throw RuntimeException("Not Implemented")
        }

        val string: String
            get() = value.toString()
    }

    @Keep
    enum class SendResultOptions(val value: String) {
        NONE("NONE"),
        LAST_RESULT("LAST_RESULT"),
        COMPLETE_RESULT("COMPLETE_RESULT");

        @Deprecated("please use .string instead", ReplaceWith("string"), level = DeprecationLevel.ERROR)
        override fun toString(): String {
            throw RuntimeException("Not Implemented")
        }

        val string: String
            get() = value.toString()
    }

    /**
     * https://techdocs.zebra.com/datawedge/latest/guide/api/resultinfo/
     * */
    @Keep
    enum class ResultCodes(val value: String) {
        APP_ALREADY_ASSOCIATED("APP_ALREADY_ASSOCIATED"),
        BUNDLE_EMPTY("BUNDLE_EMPTY"),

        DATAWEDGE_ALREADY_DISABLED("DATAWEDGE_ALREADY_DISABLED"),
        DATAWEDGE_ALREADY_ENABLED("DATAWEDGE_ALREADY_ENABLED"),
        DATAWEDGE_DISABLED("DATAWEDGE_DISABLED"),

        INPUT_NOT_ENABLED("INPUT_NOT_ENABLED"),
        OPERATION_NOT_ALLOWED("OPERATION_NOT_ALLOWED"),
        PARAMETER_INVALID("PARAMETER_INVALID"),

        PLUGIN_NOT_SUPPORTED("PLUGIN_NOT_SUPPORTED"),
        PLUGIN_BUNDLE_INVALID("PLUGIN_BUNDLE_INVALID"),
        PLUGIN_DISABLED_IN_CONFIG("PLUGIN_DISABLED_IN_CONFIG"),

        PROFILE_ALREADY_EXISTS("PROFILE_ALREADY_EXISTS"),
        PROFILE_ALREADY_SET("PROFILE_ALREADY_SET"),
        PROFILE_DISABLED("PROFILE_DISABLED"),
        PROFILE_HAS_APP_ASSOCIATION("PROFILE_HAS_APP_ASSOCIATION"),
        PROFILE_NAME_EMPTY("PROFILE_NAME_EMPTY"),
        PROFILE_NOT_FOUND("PROFILE_NOT_FOUND"),

        SCANNER_ALREADY_DISABLED("SCANNER_ALREADY_DISABLED"),
        SCANNER_ALREADY_ENABLED("SCANNER_ALREADY_ENABLED"),
        SCANNER_DISABLE_FAILED("SCANNER_DISABLE_FAILED"),
        SCANNER_ENABLE_FAILED("SCANNER_ENABLE_FAILED"),

        ERROR_INTENT_ACTION_NOT_MATCH("ERROR_INTENT_ACTION_NOT_MATCH"),
        ERROR_INTENT_NULL("ERROR_INTENT_NULL"),
        ERROR_NO_RESULT_INFO("ERROR_NO_RESULT_INFO"),
        ERROR_NO_DATAWEDGE_STATUS_IN_RESULT("ERROR_NO_DATAWEDGE_STATUS_IN_RESULT"),

        UNKNOWN("UNKNOWN");

        @Deprecated("please use .string instead", ReplaceWith("string"), level = DeprecationLevel.ERROR)
        override fun toString(): String {
            throw RuntimeException("Not Implemented")
        }

        val string: String
            get() = value.toString()
    }

    @Keep
    enum class ResultErrorCodes(val value: String) {

    }

    @Keep
    enum class ResultInfoBundleKeys(val value: String) {
        PREVIOUS_DEFAULT_PROFILE("PREVIOUS_DEFAULT_PROFILE"),
        PREVIOUS_PROFILE("PREVIOUS_PROFILE"),
        PROFILE_NAME("PROFILE_NAME"),
        SOURCE_PROFILE_NAME("SOURCE_PROFILE_NAME"),
        RESULT_CODE("RESULT_CODE");

        @Deprecated("please use .string instead", ReplaceWith("string"), level = DeprecationLevel.ERROR)
        override fun toString(): String {
            throw RuntimeException("Not Implemented")
        }

        val string: String
            get() = value.toString()
    }

    @Keep
    enum class SoftScanTriggerOptions(val value: String) {
        START_SCANNING("START_SCANNING"),
        STOP_SCANNING("STOP_SCANNING"),
        TOGGLE_SCANNING("TOGGLE_SCANNING");

        @Deprecated("please use .string instead", ReplaceWith("string"), level = DeprecationLevel.ERROR)
        override fun toString(): String {
            throw RuntimeException("Not Implemented")
        }

        val string: String
            get() = value.toString()
    }

    @Keep
    enum class IntentDeliveryOptions(val value: Int) {
        START_ACTIVITY(0),
        START_SERVICE(1),
        BROADCAST(2);

        @Deprecated("please use .string instead", ReplaceWith("string"), level = DeprecationLevel.ERROR)
        override fun toString(): String {
            throw RuntimeException("Not Implemented")
        }

        val string: String
            get() = value.toString()
    }

    @Keep
    enum class ScanInputModeOptions(val value: Int) {
        SINGLE(1),
        UDI(2),
        MULTI_BARCODE(3),
        DOCUMENT_CAPTURE(5);

        @Deprecated("please use .string instead", ReplaceWith("string"), level = DeprecationLevel.ERROR)
        override fun toString(): String {
            throw RuntimeException("Not Implemented")
        }

        val string: String
            get() = value.toString()
    }

    @Keep
    enum class WorkflowInputSourceOptions(val value: Int) {
        IMAGER(1),
        CAMERA(2);

        @Deprecated("please use .string instead", ReplaceWith("string"), level = DeprecationLevel.ERROR)
        override fun toString(): String {
            throw RuntimeException("Not Implemented")
        }

        val string: String
            get() = value.toString()
    }

    @Keep
    enum class OCRDecoderOptions(val value: String) {
        decoder_ocr_a( "decoder_ocr_a"),
        decoder_ocr_b("decoder_ocr_b"),
        decoder_micr("decoder_micr"),
        decoder_us_currency("decoder_us_currency"),
        ocr_a_variant("ocr_a_variant"),
        ocr_b_variant("ocr_b_variant");

        @Deprecated("please use .string instead", ReplaceWith("string"), level = DeprecationLevel.ERROR)
        override fun toString(): String {
            throw RuntimeException("Not Implemented")
        }

        val string: String
            get() = value.toString()
    }

    @Keep
    enum class OCRInverseOptions(val value: Int) {
        REGULAR_ONLY(0),
        INVERSE_ONLY(1),
        AUTO(2);

        @Deprecated("please use .string instead", ReplaceWith("string"), level = DeprecationLevel.ERROR)
        override fun toString(): String {
            throw RuntimeException("Not Implemented")
        }

        val string: String
            get() = value.toString()
    }

    @Keep
    enum class OCRAVariantOptions(val value: Int) {
        FULL_ASCII(0),
        RESERVED_1(1),
        RESERVED_2(2),
        BANKING(3);

        @Deprecated("please use .string instead", ReplaceWith("string"), level = DeprecationLevel.ERROR)
        override fun toString(): String {
            throw RuntimeException("Not Implemented")
        }

        val string: String
            get() = value.toString()
    }

    @Keep
    enum class OCRBVariantOptions(val value: Int) {
        FULL_ASCII(0),
        BANKING(1),
        LIMITED(2),
        TRAVEL_DOCUMENT_1(3),
        PASSPORT(4),
        ISBN_1(6),
        ISBN_2(7),
        TRAVEL_DOCUMENT_2(8),
        VISA_TYPE_A(9),
        VISA_TYPE_B(10),
        ICAO_TRAVEL_DOCUMENT(11);

        @Deprecated("please use .string instead", ReplaceWith("string"), level = DeprecationLevel.ERROR)
        override fun toString(): String {
            throw RuntimeException("Not Implemented")
        }

        val string: String
            get() = value.toString()
    }

    @Keep
    enum class NotificationType(val value: String) {
        CONFIGURATION_UPDATE("CONFIGURATION_UPDATE"),
        PROFILE_SWITCH("PROFILE_SWITCH"),
        SCANNER_STATUS("SCANNER_STATUS"),
        WORKFLOW_STATUS("WORKFLOW_STATUS");

        @Deprecated("please use .string instead", ReplaceWith("string"), level = DeprecationLevel.ERROR)
        override fun toString(): String {
            throw RuntimeException("Not Implemented")
        }

        val string: String
            get() = value.toString()
    }

    @Keep
    enum class NotificationConfigurationUpdateStatus(val value: String) {
        PROFILE_IMPORTED("PROFILE_IMPORTED"),
        FULL_DB_IMPORTED("FULL_DB_IMPORTED");
    }

    @Keep
    enum class NotificationProfileSwitchStatus(val value: String) {
        PROFILE_IMPORTED("PROFILE_IMPORTED"),
        FULL_DB_IMPORTED("FULL_DB_IMPORTED");
    }

    @Keep
    enum class NotificationScannerStatus(val value: String) {
        WAITING("WAITING"),
        SCANNING("SCANNING"),
        CONNECTED("CONNECTED"),
        DISCONNECTED("DISCONNECTED"),
        IDLE("IDLE"),
        DISABLED("DISABLED");
    }

    @Keep
    enum class NotificationWorkflowStatus(val value: String) {
        PLUGIN_READY("PLUGIN_READY"),
        DISABLED("DISABLED"),
        WORKFLOW_READY("WORKFLOW_READY"),
        WORKFLOW_ENABLED("WORKFLOW_ENABLED"),
        SESSION_STARTED("SESSION_STARTED"),
        CAPTURING_STARTED("CAPTURING_STARTED"),
        CAPTURING_STOPPED("CAPTURING_STOPPED");
    }

    @Keep
    object ScanInputParams {
        const val MODE = "scanning_mode"
        const val MULTI_BARCODE_COUNT = "multi_barcode_count"
        const val SCANNER_SELECTION = "scanner_selection_by_identifier"
        const val TRIGGER_WAKEUP = "trigger-wakeup"
        const val ENABLED = "scanner_input_enabled"
    }

    @Keep
    object WorkflowParams {
        const val SELECTED_NAME = "selected_workflow_name"

        object Input {
            const val ENABLED = "workflow_input_enabled"
            const val SOURCE = "workflow_input_source"
        }

        object FreeFormOCR {
            const val ENABLED = "workflow_free_form_ocr_enabled"
        }
    }

    @Keep
    object OCRParams {
        const val TEMPLATE = "ocr_template"
        const val INVERSE = "inverse_ocr"
        const val ORIENTATION = "ocr_orientation"
        const val LINES = "ocr_lines"

        object Decoder {
            const val ENABLE_OCR_A = "decoder_ocr_a"
            const val ENABLE_OCR_B = "decoder_ocr_b"
            const val ENABLE_MICR = "decoder_micr"
            const val ENABLE_US_CURRENCY = "decoder_us_currency"
            const val OCR_A_VARIANT = "ocr_a_variant"
            const val OCR_B_VARIANT = "ocr_b_variant"
        }

        enum class InverseOptions(val value: Int) {
            REGULAR_ONLY(0),
            INVERSE_ONLY(1),
            AUTO(2);

            @Deprecated("please use .string instead", ReplaceWith("string"), level = DeprecationLevel.ERROR)
            override fun toString(): String {
                throw RuntimeException("Not Implemented")
            }

            val string: String
                get() = value.toString()
        }

        enum class OCRAVariant(val value: Int) {
            FULL_ASCII(0),
            RESERVED_1(1),
            RESERVED_2(2),
            BANKING(3);

            @Deprecated("please use .string instead", ReplaceWith("string"), level = DeprecationLevel.ERROR)
            override fun toString(): String {
                throw RuntimeException("Not Implemented")
            }

            val string: String
                get() = value.toString()
        }

        enum class OCRBVariant(val value: Int) {
            FULL_ASCII(0),
            BANKING(1),
            LIMITED(2),
            TRAVEL_DOCUMENT_1(3),
            PASSPORT(4),
            ISBN_1(6),
            ISBN_2(7),
            TRAVEL_DOCUMENT_2(8),
            VISA_TYPE_A(9),
            VISA_TYPE_B(10),
            ICAO_TRAVEL_DOCUMENT(11);

            @Deprecated("please use .string instead", ReplaceWith("string"), level = DeprecationLevel.ERROR)
            override fun toString(): String {
                throw RuntimeException("Not Implemented")
            }

            val string: String
                get() = value.toString()
        }

        enum class OrientationOptions(val value: Int) {
            DEGREE_0(0),
            DEGREE_270(1),
            DEGREE_180(2),
            DEGREE_90(3),
            OMNIDIRECTIONAL(4);

            @Deprecated("please use .string instead", ReplaceWith("string"), level = DeprecationLevel.ERROR)
            override fun toString(): String {
                throw RuntimeException("Not Implemented")
            }

            val string: String
                get() = value.toString()
        }

        enum class LinesOptions(val value: Int) {
            LINE_1(1),
            LINE_2(2),
            LINE_3(3);

            @Deprecated("please use .string instead", ReplaceWith("string"), level = DeprecationLevel.ERROR)
            override fun toString(): String {
                throw RuntimeException("Not Implemented")
            }

            val string: String
                get() = value.toString()
        }

        object Chars {
            const val MIN_CHARS = "ocr_min_chars"
            const val MAX_CHARS = "ocr_max_chars"
            const val CHAR_SUBSET = "ocr_subset"
            // 20 to 99
            const val QUIET_ZONE = "ocr_quiet_zone"

            const val DEFAULT_SUBSET = "!\"#$%()*+,-./0123456789<>ABCDEFGHIJKLMNOPQRSTUVWXYZ abcdefghijklmnopqrstuvwxyz\\^|"
        }

        object CheckDigit {
            // 1 to 9
            const val MODULUS = "ocr_check_digit_modulus"
            // Min length = 1, Max length = 100, default = 121212121212
            const val MULTIPLIER = "ocr_check_digit_multiplier"
            const val VALIDATION = "ocr_check_digit_validation"

            enum class ValidationOptions(val value: Int) {
                NONE(0),
                PRODUCT_ADD_RL(1),
                DIGIT_ADD_RL(2),
                PRODUCT_ADD_LR(3),
                DIGIT_ADD_LR(4),
                PRODUCT_ADD_RL_SIMPLE(5),
                DIGIT_ADD_RL_SIMPLE(6),
                HEALTH_INDUSTRY(9);

                @Deprecated("please use .string instead", ReplaceWith("string"), level = DeprecationLevel.ERROR)
                override fun toString(): String {
                    throw RuntimeException("Not Implemented")
                }

                val string: String
                    get() = value.toString()
            }
        }
    }

    @Keep
    object OCRType {
        const val IMAGER_OCR = ""
        const val CAM_OCR = ""
    }

    @Keep
    object ScanResult {
        const val SOURCE = "com.symbol.datawedge.source"
        const val TYPE = "com.symbol.datawedge.label_type"
        const val DATA = "com.symbol.datawedge.data_string"
        const val TIME = "com.symbol.datawedge.data_dispatch_time"
        const val SCANNER_ID = "com.symbol.datawedge.scanner_identifier"
        const val DECODE_MODE = "com.symbol.datawedge.decoded_mode"
    }

    @Keep
    object App {
        const val PACKAGE_NAME = "PACKAGE_NAME"
        const val ACTIVITY_LIST = "ACTIVITY_LIST"
        const val APP_LIST = "APP_LIST"
    }

    //https://techdocs.zebra.com/datawedge/15-0/guide/input/barcode/#hardwaretrigger
    @Keep
    enum class BarcodeTriggerMode(val value: Int) {
        DISABLED(0),
        ENABLED(1);

        @Deprecated("please use .string instead", ReplaceWith("string"), level = DeprecationLevel.ERROR)
        override fun toString(): String {
            throw RuntimeException("Not Implemented")
        }

        val string: String
            get() = value.toString()
    }

    @Keep
    object Plugin {
        const val NAME = "PLUGIN_NAME"
        const val CONFIG= "PLUGIN_CONFIG"

        enum class Input(val value: String) {
            BARCODE("BARCODE"),
            MSR("MSR"),
            RFID("RFID"),
            SERIAL("SERIAL"),
            VOICE("VOICE"),
            WORKFLOW("WORKFLOW");

            @Deprecated("please use .string instead", ReplaceWith("string"), level = DeprecationLevel.ERROR)
            override fun toString(): String {
                throw RuntimeException("Not Implemented")
            }

            val string: String
                get() = value.toString()
        }

        enum class Processing(val value: String) {
            BDF("BDF"),
            ADF("ADF"),
            TOKEN("TOKEN");

            @Deprecated("please use .string instead", ReplaceWith("string"), level = DeprecationLevel.ERROR)
            override fun toString(): String {
                throw RuntimeException("Not Implemented")
            }

            val string: String
                get() = value.toString()
        }

        enum class Output(val value: String) {
            INTENT("INTENT"),
            KEYSTROKE("KEYSTROKE"),
            IP("IP");

            @Deprecated("please use .string instead", ReplaceWith("string"), level = DeprecationLevel.ERROR)
            override fun toString(): String {
                throw RuntimeException("Not Implemented")
            }

            val string: String
                get() = value.toString()
        }

        enum class Utilities(val value: String) {
            DCP("DCP"),
            EKB("EKB");

            @Deprecated("please use .string instead", ReplaceWith("string"), level = DeprecationLevel.ERROR)
            override fun toString(): String {
                throw RuntimeException("Not Implemented")
            }

            val string: String
                get() = value.toString()
        }

        enum class WorkflowName(val value: String) {
            LICENSE_PLATE("license_plate"),
            ID_SCANNING("id_scanning"),
            VIN_NUMBER("vin_number"),
            TIN_NUMBER("tin_number"),
            CONTAINER_SCANNING("container_scanning"),
            METER_READING("meter_reading"),
            FREE_FORM_CAPTURE("free_form_capture"),
            DOCUMENT_CAPTURE("document_capture"),
            PICKLIST_OCR("picklist_ocr"),
            FREE_FORM_OCR("free_form_ocr");

            @Deprecated("please use .string instead", ReplaceWith("string"), level = DeprecationLevel.ERROR)
            override fun toString(): String {
                throw RuntimeException("Not Implemented")
            }

            val string: String
                get() = value.toString()
        }
    }

    @Keep
    object DCPParams {
        const val ENABLED = "dcp_input_enabled"
        const val DOCK = "dcp_dock_button_on"
        const val MODE = "dcp_start_in"
        // 0-100
        const val HIGH_POS = "dcp_highest_pos"
        const val LOW_POS = "dcp_lowest_pos"
        // 0-1000
        const val DRAG = "dcp_drag_detect_time"

        object DockOptions {
            const val LEFT = "LEFT"
            const val RIGHT = "RIGHT"
            const val BOTH = "BOTH"
        }

        object ModeOptions {
            const val FULLSCREEN = "FULLSCREEN"
            const val BUTTON = "BUTTON"
            const val BUTTON_ONLY = "BUTTON_ONLY"
        }
    }

    @Keep
    object IntentParams {
        const val OUTPUT_ENABLED = "intent_output_enabled"
        const val ACTION = "intent_action"
        const val CATEGORY = "intent_category"
        const val DELIVERY = "intent_delivery"

        enum class DeliveryOptions(val value: Int) {
            START_ACTIVITY(0),
            START_SERVICE(1),
            BROADCAST(2);

            @Deprecated("please use .string instead", ReplaceWith("string"), level = DeprecationLevel.ERROR)
            override fun toString(): String {
                throw RuntimeException("Not Implemented")
            }

            val string: String
                get() = value.toString()
        }
    }

    @Keep
    object Command {
        const val COMMAND = "COMMAND"
        const val COMMAND_IDENTIFIER = "COMMAND_IDENTIFIER"
        const val COMMAND_ID_CREATE_PROFILE = "COMMAND_ID_CREATE_PROFILE_123"
    }

    @Keep
    object Result {
        const val SEND_RESULT = "SEND_RESULT"
        const val RESULT = "RESULT"
        const val RESULT_INFO = "RESULT_INFO"
        const val RESULT_CODE = "RESULT_CODE"
    }

    @Keep
    object Profile {
        const val NAME = "PROFILE_NAME"
        const val CONFIG_MODE = "CONFIG_MODE"
        const val ENABLED = "PROFILE_ENABLED"
    }

    @Keep
    object BundleParams {
        const val RESET_CONFIG = "RESET_CONFIG"
        const val PLUGIN_NAME = "PLUGIN_NAME"
        const val PARAM_LIST = "PARAM_LIST"
        const val OUTPUT_PLUGIN_NAME = "OUTPUT_PLUGIN_NAME"
    }

    @Keep
    object BDFParams {
        const val ENABLED = "bdf_enabled"
        const val SEND_DATA = "bdf_send_data"
    }

    @Keep
    object KeyStrokeParams {
        const val OUTPUT_ENABLED = "keystroke_output_enabled"
        const val ACTION_CHAR = "keystroke_action_char"
        const val DELAY_EXTENDED_ASCII = "keystroke_delay_extended_ascii"
        const val DELAY_CONTROL_CHARS = "keystroke_delay_control_chars"
    }
}