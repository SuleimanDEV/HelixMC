package club.helix.components.moshi

import club.helix.components.account.HelixRankLife
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson

class MoshiListAdapter {

    @ToJson
    fun arrayListToJson(list: List<HelixRankLife>): MutableList<HelixRankLife> =
        list.toMutableList()

    @FromJson
    fun arrayListFromJson(mutableList: MutableList<HelixRankLife>): List<HelixRankLife> =
        mutableList.toList()
}