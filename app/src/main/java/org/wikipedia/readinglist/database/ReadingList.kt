package org.wikipedia.readinglist.database

import android.text.TextUtils
import org.apache.commons.lang3.StringUtils
import org.wikipedia.R
import org.wikipedia.WikipediaApp
import org.wikipedia.readinglist.database.ReadingListPage
import java.io.Serializable
import java.util.*

class ReadingList(var description: String?,
                  val pages: MutableList<ReadingListPage>,
                  var id: Long = 0,
                  var mtime: Long,
                  var atime: Long,
                  var sizeBytes: Long,
                  var dirty: Boolean = true,
                  var remoteId: Long = 0) : Serializable {

    @Transient
    private var accentAndCaseInvariantTitle: String? = null

    fun numPagesOffline(): Int {
        var count = 0
        for (page in pages) {
            if (page.offline() && page.status() == ReadingListPage.STATUS_SAVED) {
                count++
            }
        }
        return count
    }

    var title: String? = null
        get() = if (isDefault) WikipediaApp.getInstance().getString(R.string.default_reading_list_name) else field

    val isDefault = title.isNullOrEmpty()

    val dbTitle = title.orEmpty()

    fun accentAndCaseInvariantTitle(): String {
        if (accentAndCaseInvariantTitle == null) {
            accentAndCaseInvariantTitle = StringUtils.stripAccents(title).toLowerCase(Locale.getDefault())
        }
        return accentAndCaseInvariantTitle as String
    }

    fun touch() {
        atime = System.currentTimeMillis()
    }

    val sizeBytesFromPages: Long
        get() {
            var bytes: Long = 0
            for (page in pages) {
                bytes += if (page.offline()) page.sizeBytes() else 0
            }
            return bytes
        }

    init {
        val now = System.currentTimeMillis()
        mtime = now
        atime = now
    }

    companion object {
        const val SORT_BY_NAME_ASC = 0
        const val SORT_BY_NAME_DESC = 1
        const val SORT_BY_RECENT_ASC = 2
        const val SORT_BY_RECENT_DESC = 3
        @JvmField
        val DATABASE_TABLE = ReadingListTable()
        fun sort(list: ReadingList, sortMode: Int) {
            when (sortMode) {
                SORT_BY_NAME_ASC -> list.pages.sortWith { lhs: ReadingListPage, rhs: ReadingListPage -> lhs.accentAndCaseInvariantTitle().compareTo(rhs.accentAndCaseInvariantTitle()) }
                SORT_BY_NAME_DESC -> list.pages.sortWith { lhs: ReadingListPage, rhs: ReadingListPage -> rhs.accentAndCaseInvariantTitle().compareTo(lhs.accentAndCaseInvariantTitle()) }
                SORT_BY_RECENT_ASC -> list.pages.sortWith { lhs: ReadingListPage, rhs: ReadingListPage -> lhs.mtime().compareTo(rhs.mtime()) }
                SORT_BY_RECENT_DESC -> list.pages.sortWith { lhs: ReadingListPage, rhs: ReadingListPage -> rhs.mtime().compareTo(lhs.mtime()) }
                else -> {
                }
            }
        }

        fun sort(lists: MutableList<ReadingList>, sortMode: Int) {
            when (sortMode) {
                SORT_BY_NAME_ASC -> lists.sortWith { lhs: ReadingList, rhs: ReadingList -> lhs.accentAndCaseInvariantTitle().compareTo(rhs.accentAndCaseInvariantTitle()) }
                SORT_BY_NAME_DESC -> lists.sortWith { lhs: ReadingList, rhs: ReadingList -> rhs.accentAndCaseInvariantTitle().compareTo(lhs.accentAndCaseInvariantTitle()) }
                SORT_BY_RECENT_ASC -> lists.sortWith { lhs: ReadingList, rhs: ReadingList -> rhs.mtime.compareTo(lhs.mtime) }
                SORT_BY_RECENT_DESC -> lists.sortWith { lhs: ReadingList, rhs: ReadingList -> lhs.mtime.compareTo(rhs.mtime) }
                else -> {
                }
            }
            // make the Default list sticky on top, regardless of sorting.
            var defaultList: ReadingList? = null
            for (list in lists) {
                if (list.isDefault) {
                    defaultList = list
                    break
                }
            }
            if (defaultList != null) {
                lists.remove(defaultList)
                lists.add(0, defaultList)
            }
        }

        fun sortGenericList(lists: MutableList<Any>, sortMode: Int) {
            when (sortMode) {
                SORT_BY_NAME_ASC -> lists.sortWith { lhs: Any?, rhs: Any? ->
                    if (lhs is ReadingList && rhs is ReadingList) {
                        lhs.accentAndCaseInvariantTitle().compareTo(rhs.accentAndCaseInvariantTitle())
                    } else {
                        0
                    }
                }
                SORT_BY_NAME_DESC -> lists.sortWith { lhs: Any?, rhs: Any? ->
                    if (lhs is ReadingList && rhs is ReadingList) {
                        rhs.accentAndCaseInvariantTitle().compareTo(lhs.accentAndCaseInvariantTitle())
                    } else {
                        0
                    }
                }
                SORT_BY_RECENT_ASC -> lists.sortWith { lhs: Any?, rhs: Any? ->
                    if (lhs is ReadingList && rhs is ReadingList) {
                        rhs.mtime.compareTo(lhs.mtime)
                    } else {
                        0
                    }
                }
                SORT_BY_RECENT_DESC -> lists.sortWith { lhs: Any?, rhs: Any? ->
                    if (lhs is ReadingList && rhs is ReadingList) {
                        lhs.mtime.compareTo(rhs.mtime)
                    } else {
                        0
                    }
                }
                else -> {
                }
            }
            // make the Default list sticky on top, regardless of sorting.
            var defaultList: ReadingList? = null
            for (list in lists) {
                if (list is ReadingList && list.isDefault) {
                    defaultList = list
                    break
                }
            }
            if (defaultList != null) {
                lists.remove(defaultList)
                lists.add(0, defaultList)
            }
        }
    }
}
