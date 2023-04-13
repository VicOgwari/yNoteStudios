package com.midland.ynote.Utilities

import com.midland.ynote.Adapters.CloudVideosAdapter
import com.midland.ynote.Adapters.DocumentAdapter
import com.midland.ynote.Adapters.SubFieldAdt
import com.midland.ynote.Objects.SelectedDoc
import java.security.MessageDigest

class DocRetrieval {

    companion object Companion {

        var yearFilterString = String()
        var photoFlag = String()
        var photoFlag1 = String()

        var userDocAdt: DocumentAdapter? = null
        var userLecAdt: CloudVideosAdapter? = null

        var agr0 = ArrayList<SelectedDoc>()
        var agrTags0 = ArrayList<String>()
        var agrAdapter0: DocumentAdapter? = null
        var agrLecAdapter0: CloudVideosAdapter? = null
        var agr1 = ArrayList<SelectedDoc>()
        var agrTags1 = ArrayList<String>()
        var agrAdapter1: DocumentAdapter? = null
        var agrLecAdapter1: CloudVideosAdapter? = null
        var agr2 = ArrayList<SelectedDoc>()
        var agrTags2 = ArrayList<String>()
        var agrAdapter2: DocumentAdapter? = null
        var agrLecAdapter2: CloudVideosAdapter? = null
        var agrAdapters = ArrayList<DocumentAdapter>()
        var agrLecAdapters = ArrayList<CloudVideosAdapter>()
        var agrLectures = ArrayList<SelectedDoc>()
        var agrTagsArray = ArrayList<SubFieldAdt>()


        var appHumanSci0 = ArrayList<SelectedDoc>()
        var appHumanSciTags0 = ArrayList<String>()
        var appHumanAdapter0: DocumentAdapter? = null
        var appLecHumanAdapter0: CloudVideosAdapter? = null
        var appHumanSci1 = ArrayList<SelectedDoc>()
        var appHumanSciTags1 = ArrayList<String>()
        var appHumanAdapter1: DocumentAdapter? = null
        var appLecHumanAdapter1: CloudVideosAdapter? = null
        var appHumanSci2 = ArrayList<SelectedDoc>()
        var appHumanSciTags2 = ArrayList<String>()
        var appHumanAdapter2: DocumentAdapter? = null
        var appLecHumanAdapter2: CloudVideosAdapter? = null
        var appHumanSci3 = ArrayList<SelectedDoc>()
        var appHumanSciTags3 = ArrayList<String>()
        var appHumanAdapter3: DocumentAdapter? = null
        var appLecHumanAdapter3: CloudVideosAdapter? = null
        var appHumanSci4 = ArrayList<SelectedDoc>()
        var appHumanSciTags4 = ArrayList<String>()
        var appHumanAdapter4: DocumentAdapter? = null
        var appLecHumanAdapter4: CloudVideosAdapter? = null
        var appHumanAdapters = ArrayList<DocumentAdapter>()
        var appLecHumanAdapters = ArrayList<CloudVideosAdapter>()
        var appHumanSciLectures = ArrayList<SelectedDoc>()
        var appHumanSciTagsArray = ArrayList<SubFieldAdt>()




        var biz0 = ArrayList<SelectedDoc>()
        var bizTags0 = ArrayList<String>()
        var bizAdapter0: DocumentAdapter? = null
        var bizLecAdapter0: CloudVideosAdapter? = null
        var biz1 = ArrayList<SelectedDoc>()
        var bizTags1 = ArrayList<String>()
        var bizAdapter1: DocumentAdapter? = null
        var bizLecAdapter1: CloudVideosAdapter? = null
        var biz2 = ArrayList<SelectedDoc>()
        var bizTags2 = ArrayList<String>()
        var bizAdapter2: DocumentAdapter? = null
        var bizLecAdapter2: CloudVideosAdapter? = null
        var bizAdapters = ArrayList<DocumentAdapter>()
        var bizLecAdapters = ArrayList<CloudVideosAdapter>()
        var bizLectures = ArrayList<SelectedDoc>()
        var bizTagsArray = ArrayList<SubFieldAdt>()





        var econ0 = ArrayList<SelectedDoc>()
        var econTags0 = ArrayList<String>()
        var econAdapter0: DocumentAdapter? = null
        var econLecAdapter0: CloudVideosAdapter? = null
        var econ1 = ArrayList<SelectedDoc>()
        var econTags1 = ArrayList<String>()
        var econAdapter1: DocumentAdapter? = null
        var econLecAdapter1: CloudVideosAdapter? = null
        var econ2 = ArrayList<SelectedDoc>()
        var econTags2 = ArrayList<String>()
        var econAdapter2: DocumentAdapter? = null
        var econLecAdapter2: CloudVideosAdapter? = null
        var econAdapters = ArrayList<DocumentAdapter>()
        var econLecAdapters = ArrayList<CloudVideosAdapter>()
        var econLectures = ArrayList<SelectedDoc>()
        var econTagsArray = ArrayList<SubFieldAdt>()




        var education0 = ArrayList<SelectedDoc>()
        var eduTags0 = ArrayList<String>()
        var eduAdapter0: DocumentAdapter? = null
        var eduLecAdapter0: CloudVideosAdapter? = null
        var education1 = ArrayList<SelectedDoc>()
        var eduTags1 = ArrayList<String>()
        var eduAdapter1: DocumentAdapter? = null
        var eduLecAdapter1: CloudVideosAdapter? = null
        var education2 = ArrayList<SelectedDoc>()
        var eduTags2 = ArrayList<String>()
        var eduAdapter2: DocumentAdapter? = null
        var eduLecAdapter2: CloudVideosAdapter? = null
        var education3 = ArrayList<SelectedDoc>()
        var eduTags3 = ArrayList<String>()
        var eduAdapter3: DocumentAdapter? = null
        var eduLecAdapter3: CloudVideosAdapter? = null
        var education4 = ArrayList<SelectedDoc>()
        var eduTags4 = ArrayList<String>()
        var eduAdapter4: DocumentAdapter? = null
        var eduLecAdapter4: CloudVideosAdapter? = null
        var education5 = ArrayList<SelectedDoc>()
        var eduTags5 = ArrayList<String>()
        var eduAdapter5: DocumentAdapter? = null
        var eduLecAdapter5: CloudVideosAdapter? = null
        var education6 = ArrayList<SelectedDoc>()
        var eduTags6 = ArrayList<String>()
        var eduAdapter6: DocumentAdapter? = null
        var eduLecAdapter6: CloudVideosAdapter? = null
        var eduAdapters = ArrayList<DocumentAdapter>()
        var eduLecAdapters = ArrayList<CloudVideosAdapter>()
        var educationLectures = ArrayList<SelectedDoc>()
        var educationTagsArray = ArrayList<SubFieldAdt>()




        var engineering0 = ArrayList<SelectedDoc>()
        var engTags0 = ArrayList<String>()
        var engAdapter0: DocumentAdapter? = null
        var engLecAdapter0: CloudVideosAdapter? = null
        var engineering1 = ArrayList<SelectedDoc>()
        var engTags1 = ArrayList<String>()
        var engAdapter1: DocumentAdapter? = null
        var engLecAdapter1: CloudVideosAdapter? = null
        var engineering2 = ArrayList<SelectedDoc>()
        var engTags2 = ArrayList<String>()
        var engAdapter2: DocumentAdapter? = null
        var engLecAdapter2: CloudVideosAdapter? = null
        var engineering3 = ArrayList<SelectedDoc>()
        var engTags3 = ArrayList<String>()
        var engAdapter3: DocumentAdapter? = null
        var engLecAdapter3: CloudVideosAdapter? = null
        var engineering4 = ArrayList<SelectedDoc>()
        var engTags4 = ArrayList<String>()
        var engAdapter4: DocumentAdapter? = null
        var engLecAdapter4: CloudVideosAdapter? = null
        var engineering5 = ArrayList<SelectedDoc>()
        var engTags5 = ArrayList<String>()
        var engAdapter5: DocumentAdapter? = null
        var engLecAdapter5: CloudVideosAdapter? = null
        var engAdapters = ArrayList<DocumentAdapter>()
        var engLecAdapters = ArrayList<CloudVideosAdapter>()
        var engineeringLectures = ArrayList<SelectedDoc>()
        var engTagsArray = ArrayList<SubFieldAdt>()




        var environmental0 = ArrayList<SelectedDoc>()
        var envTags0 = ArrayList<String>()
        var envAdapter0: DocumentAdapter? = null
        var envLecAdapter0: CloudVideosAdapter? = null
        var environmental1 = ArrayList<SelectedDoc>()
        var envTags1 = ArrayList<String>()
        var envAdapter1: DocumentAdapter? = null
        var envLecAdapter1: CloudVideosAdapter? = null
        var environmental2 = ArrayList<SelectedDoc>()
        var envTags2 = ArrayList<String>()
        var envAdapter2: DocumentAdapter? = null
        var envLecAdapter2: CloudVideosAdapter? = null
        var environmental3 = ArrayList<SelectedDoc>()
        var envTags3 = ArrayList<String>()
        var envAdapter3: DocumentAdapter? = null
        var envLecAdapter3: CloudVideosAdapter? = null
        var envAdapters = ArrayList<DocumentAdapter>()
        var envLecAdapters = ArrayList<CloudVideosAdapter>()
        var environmentalLectures = ArrayList<SelectedDoc>()
        var envTagsArray = ArrayList<SubFieldAdt>()



        var hospitality0 = ArrayList<SelectedDoc>()
        var hospitalityTags0 = ArrayList<String>()
        var hospitalityAdapter0: DocumentAdapter? = null
        var hospitalityLecAdapter0: CloudVideosAdapter? = null
        var hospitality1 = ArrayList<SelectedDoc>()
        var hospitalityTags1 = ArrayList<String>()
        var hospitalityAdapter1: DocumentAdapter? = null
        var hospitalityLecAdapter1: CloudVideosAdapter? = null
        var hospitalityAdapters = ArrayList<DocumentAdapter>()
        var hospitalityLecAdapters = ArrayList<CloudVideosAdapter>()
        var hospitalityLectures = ArrayList<SelectedDoc>()
        var hospitalityTagsArray = ArrayList<SubFieldAdt>()


        var humanities0 = ArrayList<SelectedDoc>()
        var humanitiesTags0 = ArrayList<String>()
        var humanitiesAdapter0: DocumentAdapter? = null
        var humanitiesLecAdapter0: CloudVideosAdapter? = null
        var humanities1 = ArrayList<SelectedDoc>()
        var humanitiesTags1 = ArrayList<String>()
        var humanitiesAdapter1: DocumentAdapter? = null
        var humanitiesLecAdapter1: CloudVideosAdapter? = null
        var humanities2 = ArrayList<SelectedDoc>()
        var humanitiesTags2 = ArrayList<String>()
        var humanitiesAdapter2: DocumentAdapter? = null
        var humanitiesLecAdapter2: CloudVideosAdapter? = null
        var humanities3 = ArrayList<SelectedDoc>()
        var humanitiesTags3 = ArrayList<String>()
        var humanitiesAdapter3: DocumentAdapter? = null
        var humanitiesLecAdapter3: CloudVideosAdapter? = null
        var humanities4 = ArrayList<SelectedDoc>()
        var humanitiesTags4 = ArrayList<String>()
        var humanitiesAdapter4: DocumentAdapter? = null
        var humanitiesLecAdapter4: CloudVideosAdapter? = null
        var humanities5 = ArrayList<SelectedDoc>()
        var humanitiesTags5 = ArrayList<String>()
        var humanitiesAdapter5: DocumentAdapter? = null
        var humanitiesLecAdapter5: CloudVideosAdapter? = null
        var humanities6 = ArrayList<SelectedDoc>()
        var humanitiesTags6 = ArrayList<String>()
        var humanitiesAdapter6: DocumentAdapter? = null
        var humanitiesLecAdapter6: CloudVideosAdapter? = null
        var humanities7 = ArrayList<SelectedDoc>()
        var humanitiesTags7 = ArrayList<String>()
        var humanitiesAdapter7: DocumentAdapter? = null
        var humanitiesLecAdapter7: CloudVideosAdapter? = null
        var humanities8 = ArrayList<SelectedDoc>()
        var humanitiesTags8 = ArrayList<String>()
        var humanitiesAdapter8: DocumentAdapter? = null
        var humanitiesLecAdapter8: CloudVideosAdapter? = null
        var humanities9 = ArrayList<SelectedDoc>()
        var humanitiesTags9 = ArrayList<String>()
        var humanitiesAdapter9: DocumentAdapter? = null
        var humanitiesLecAdapter9: CloudVideosAdapter? = null
        var humanities10 = ArrayList<SelectedDoc>()
        var humanitiesTags10 = ArrayList<String>()
        var humanitiesAdapter10: DocumentAdapter? = null
        var humanitiesLecAdapter10: CloudVideosAdapter? = null
        var humanitiesAdapters = ArrayList<DocumentAdapter>()
        var humanitiesLecAdapters = ArrayList<CloudVideosAdapter>()
        var humanitiesLectures = ArrayList<SelectedDoc>()
        var humanitiesTagsArray = ArrayList<SubFieldAdt>()



        var law0 = ArrayList<SelectedDoc>()
        var lawTags0 = ArrayList<String>()
        var lawAdapter0: DocumentAdapter? = null
        var lawLecAdapter0: CloudVideosAdapter? = null
        var law1 = ArrayList<SelectedDoc>()
        var lawTags1 = ArrayList<String>()
        var lawAdapter1: DocumentAdapter? = null
        var lawLecAdapter1: CloudVideosAdapter? = null
        var law2 = ArrayList<SelectedDoc>()
        var lawTags2 = ArrayList<String>()
        var lawAdapter2: DocumentAdapter? = null
        var lawLecAdapter2: CloudVideosAdapter? = null
        var law3 = ArrayList<SelectedDoc>()
        var lawTags3 = ArrayList<String>()
        var lawAdapter3: DocumentAdapter? = null
        var lawLecAdapter3: CloudVideosAdapter? = null
        var law4 = ArrayList<SelectedDoc>()
        var lawTags4 = ArrayList<String>()
        var lawAdapter4: DocumentAdapter? = null
        var lawLecAdapter4: CloudVideosAdapter? = null
        var law5 = ArrayList<SelectedDoc>()
        var lawTags5 = ArrayList<String>()
        var lawAdapter5: DocumentAdapter? = null
        var lawLecAdapter5: CloudVideosAdapter? = null
        var lawAdapters = ArrayList<DocumentAdapter>()
        var lawLecAdapters = ArrayList<CloudVideosAdapter>()
        var lawLectures = ArrayList<SelectedDoc>()
        var lawTagsArray = ArrayList<SubFieldAdt>()



        var medicine0 = ArrayList<SelectedDoc>()
        var medTags0 = ArrayList<String>()
        var medAdapter0: DocumentAdapter? = null
        var medLecAdapter0: CloudVideosAdapter? = null
        var medicine1 = ArrayList<SelectedDoc>()
        var medTags1 = ArrayList<String>()
        var medAdapter1: DocumentAdapter? = null
        var medLecAdapter1: CloudVideosAdapter? = null
        var medicine2 = ArrayList<SelectedDoc>()
        var medTags2 = ArrayList<String>()
        var medAdapter2: DocumentAdapter? = null
        var medLecAdapter2: CloudVideosAdapter? = null
        var medicine3 = ArrayList<SelectedDoc>()
        var medTags3 = ArrayList<String>()
        var medAdapter3: DocumentAdapter? = null
        var medLecAdapter3: CloudVideosAdapter? = null
        var medicine4 = ArrayList<SelectedDoc>()
        var medTags4 = ArrayList<String>()
        var medAdapter4: DocumentAdapter? = null
        var medLecAdapter4: CloudVideosAdapter? = null
        var medicine5 = ArrayList<SelectedDoc>()
        var medTags5 = ArrayList<String>()
        var medAdapter5: DocumentAdapter? = null
        var medLecAdapter5: CloudVideosAdapter? = null
        var medicine6 = ArrayList<SelectedDoc>()
        var medTags6 = ArrayList<String>()
        var medAdapter6: DocumentAdapter? = null
        var medLecAdapter6: CloudVideosAdapter? = null
        var medicine7 = ArrayList<SelectedDoc>()
        var medTags7 = ArrayList<String>()
        var medAdapter7: DocumentAdapter? = null
        var medLecAdapter7: CloudVideosAdapter? = null
        var medicine8 = ArrayList<SelectedDoc>()
        var medTags8 = ArrayList<String>()
        var medAdapter8: DocumentAdapter? = null
        var medLecAdapter8: CloudVideosAdapter? = null
        var medicine9 = ArrayList<SelectedDoc>()
        var medTags9 = ArrayList<String>()
        var medAdapter9: DocumentAdapter? = null
        var medLecAdapter9: CloudVideosAdapter? = null
        var medAdapters = ArrayList<DocumentAdapter>()
        var medLecAdapters = ArrayList<CloudVideosAdapter>()
        var medicineLectures = ArrayList<SelectedDoc>()
        var medTagsArray = ArrayList<SubFieldAdt>()



        var publicHealth0 = ArrayList<SelectedDoc>()
        var publicHealthTags0 = ArrayList<String>()
        var publicHealthAdapter0: DocumentAdapter? = null
        var publicHealthLecAdapter0: CloudVideosAdapter? = null
        var publicHealth1 = ArrayList<SelectedDoc>()
        var publicHealthTags1 = ArrayList<String>()
        var publicHealthAdapter1: DocumentAdapter? = null
        var publicHealthLecAdapter1: CloudVideosAdapter? = null
        var publicHealth2 = ArrayList<SelectedDoc>()
        var publicHealthTags2 = ArrayList<String>()
        var publicHealthAdapter2: DocumentAdapter? = null
        var publicHealthLecAdapter2: CloudVideosAdapter? = null
        var publicHealth3 = ArrayList<SelectedDoc>()
        var publicHealthTags3 = ArrayList<String>()
        var publicHealthAdapter3: DocumentAdapter? = null
        var publicHealthLecAdapter3: CloudVideosAdapter? = null
        var publicHealthAdapters = ArrayList<DocumentAdapter>()
        var publicHealthLecAdapters = ArrayList<CloudVideosAdapter>()
        var publicHealthLectures = ArrayList<SelectedDoc>()
        var publicHealthTagsArray = ArrayList<SubFieldAdt>()



        var pureAppSci0 = ArrayList<SelectedDoc>()
        var pureAppTags0 = ArrayList<String>()
        var docAdapter0: DocumentAdapter? = null
        var docLecAdapter0: CloudVideosAdapter? = null
        var pureAppSci1 = ArrayList<SelectedDoc>()
        var pureAppTags1 = ArrayList<String>()
        var docAdapter1: DocumentAdapter? = null
        var docLecAdapter1: CloudVideosAdapter? = null
        var pureAppSci2 = ArrayList<SelectedDoc>()
        var pureAppTags2 = ArrayList<String>()
        var docAdapter2: DocumentAdapter? = null
        var docLecAdapter2: CloudVideosAdapter? = null
        var pureAppSci3 = ArrayList<SelectedDoc>()
        var pureAppTags3 = ArrayList<String>()
        var docAdapter3: DocumentAdapter? = null
        var docLecAdapter3: CloudVideosAdapter? = null
        var pureAppSci4 = ArrayList<SelectedDoc>()
        var pureAppTags4 = ArrayList<String>()
        var docAdapter4: DocumentAdapter? = null
        var docLecAdapter4: CloudVideosAdapter? = null
        var pureAppSci5 = ArrayList<SelectedDoc>()
        var pureAppTags5 = ArrayList<String>()
        var docAdapter5: DocumentAdapter? = null
        var docLecAdapter5: CloudVideosAdapter? = null
        var pureAppSci6 = ArrayList<SelectedDoc>()
        var pureAppTags6 = ArrayList<String>()
        var docAdapter6: DocumentAdapter? = null
        var docLecAdapter6: CloudVideosAdapter? = null
        var pureAppSci7 = ArrayList<SelectedDoc>()
        var pureAppTags7 = ArrayList<String>()
        var docAdapter7: DocumentAdapter? = null
        var docLecAdapter7: CloudVideosAdapter? = null
        var pureAdapters = ArrayList<DocumentAdapter>()
        var pureLecAdapters = ArrayList<CloudVideosAdapter>()
        var pureAppSciLectures = ArrayList<SelectedDoc>()
        var pureAppTagsArray = ArrayList<SubFieldAdt>()



        var confuciusAdapter: DocumentAdapter? = null
        var confuciusLecAdapter: CloudVideosAdapter? = null
        var confucius = ArrayList<SelectedDoc>()
        var confuciusLectures = ArrayList<SelectedDoc>()
        var confuciusTags = ArrayList<String>()
        var TagsArray = ArrayList<ArrayList<String>>()
        var confuciusTagsArray = ArrayList<SubFieldAdt>()



        var peaceAdapter: DocumentAdapter? = null
        var peaceLecAdapter: CloudVideosAdapter? = null
        var peaceSecurity7 = ArrayList<SelectedDoc>()
        var peaceSecurityLectures = ArrayList<SelectedDoc>()
        var peaceSecurityLecLectures = ArrayList<CloudVideosAdapter>()
        var peaceSecurityTags = ArrayList<String>()
        var peaceSecurityTagsArray = ArrayList<SubFieldAdt>()

        var visualArt0 = ArrayList<SelectedDoc>()
        var visualArtTags0 = ArrayList<String>()
        var visualAdapter0: DocumentAdapter? = null
        var visualLecAdapter0: CloudVideosAdapter? = null
        var visualArt1 = ArrayList<SelectedDoc>()
        var visualArtTags1 = ArrayList<String>()
        var visualAdapter1: DocumentAdapter? = null
        var visualLecAdapter1: CloudVideosAdapter? = null
        var visualArt2 = ArrayList<SelectedDoc>()
        var visualArtTags2 = ArrayList<String>()
        var visualAdapter2: DocumentAdapter? = null
        var visualLecAdapter2: CloudVideosAdapter? = null
        var visualAdapters = ArrayList<DocumentAdapter>()
        var visualLecAdapters = ArrayList<CloudVideosAdapter>()
        var visualArtLectures = ArrayList<SelectedDoc>()
        var visualArtTagsArray = ArrayList<SubFieldAdt>()


        var creativeArt0 = ArrayList<SelectedDoc>()
        var creativeArtTags0 = ArrayList<String>()
        var creativeArtAdapter0: DocumentAdapter? = null
        var creativeArtLecAdapter0: CloudVideosAdapter? = null
        var creativeArt1 = ArrayList<SelectedDoc>()
        var creativeArtTags1 = ArrayList<String>()
        var creativeArtAdapter1: DocumentAdapter? = null
        var creativeArtLecAdapter1: CloudVideosAdapter? = null
        var creativeArtAdapters = ArrayList<DocumentAdapter>()
        var creativeArtLectures = ArrayList<SelectedDoc>()
        var creativeArtTagsArray = ArrayList<SubFieldAdt>()


        var architecture0 = ArrayList<SelectedDoc>()
        var architectureTags0 = ArrayList<String>()
        var architectureAdapter0: DocumentAdapter? = null
        var architectureLecAdapter0: CloudVideosAdapter? = null
        var architecture1 = ArrayList<SelectedDoc>()
        var architectureTags1 = ArrayList<String>()
        var architectureAdapter1: DocumentAdapter? = null
        var architectureLecAdapter1: CloudVideosAdapter? = null
        var architecture2 = ArrayList<SelectedDoc>()
        var architectureTags2 = ArrayList<String>()
        var architectureAdapter2: DocumentAdapter? = null
        var architectureLecAdapter2: CloudVideosAdapter? = null
        var architecture3 = ArrayList<SelectedDoc>()
        var architectureTags3 = ArrayList<String>()
        var architectureAdapter3: DocumentAdapter? = null
        var architectureLecAdapter3: CloudVideosAdapter? = null
        var architectureAdapters = ArrayList<DocumentAdapter>()
        var architectureLectures = ArrayList<SelectedDoc>()
        var architectureTagsArray = ArrayList<SubFieldAdt>()
        var architectureLecAdapters = ArrayList<CloudVideosAdapter>()



        fun priceGenerator(pins: Int): String? {
            var price = "0"
            if (pins == 20) {
                price = "26"
            }
            if (pins in 21..30) {
                price = "37"
            }
            if (pins in 31..40) {
                price = "43"
            }
            if (pins in 41..50) {
                price = "52"
            }
            if (pins in 51..60) {
                price = "66"
            }
            if (pins in 61..70) {
                price = "72"
            }
            if (pins in 71..90) {
                price = "89"
            }
            if (pins in 91..100) {
                price = "95"
            }
            if (pins > 100) {
                val topUp = pins - 100
                price = (105 + (topUp/2)).toString()
            }

            if (Integer.parseInt(price) > 599){
                price = "599"
            }
            return price
        }

        fun checkCount(countStr: String): String {
//            val countInt = countStr.toInt()
//            var returnStr = ""
//            if (countInt < 999) {
//                returnStr = countInt.toString()
//            } else if (countInt in 1000..999998) {
//                returnStr = String.format("%.1f", countInt / 1000) + "K"
//            } else if (countInt in 1000000..999999998) {
//                returnStr = String.format("%.1f", countInt / 1000000) + "M"
//            } else if (countInt > 999999999) {
//                returnStr = String.format("%.1f", countInt / 1000000000) + "B"
//            }
            return countStr
        }
    }

    @Throws(Exception::class)
    fun shaEncryption(data: ByteArray?, shaN: String?): ByteArray? {
        val digest = shaN?.let { MessageDigest.getInstance(it) }
        data?.let { digest!!.update(it) }
        return digest!!.digest()
    }

//    @Throws(
//        NoSuchAlgorithmException::class,
//        NoSuchPaddingException::class,
//        InvalidKeyException::class,
//        InvalidParameterException::class,
//        InvalidKeyException::class,
//        BadPaddingException::class,
//        IllegalBlockSizeException::class,
//        UnsupportedEncodingException::class
//    )
//    fun decryptCase(
//        cipherText: String,
//        key: SecretKey
//    ): String {
//        val cipher: Cipher = Cipher.getInstance("AES/ECB/PKCS5padding")
//        cipher.init(Cipher.DECRYPT_MODE, key)
//        val decode = Base64.decode(cipherText, Base64.NO_WRAP)
//        return String(cipher.doFinal(decode), "UTF-8")
//    }
}

