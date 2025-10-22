package org.company.app.network

import androidx.compose.runtime.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import org.company.app.utilsUI.ExpandableList
import org.company.app.utilsUI.createGroups

@Serializable
data class Room(
    val title: String,
    val freetime: Int
)

val roomsName = listOf("Halle", "Germain", "Gouges")

class RoomViewModel {
    private val _expandableList = mutableStateOf<ExpandableList<Room>?>(null)
    val expandableList: State<ExpandableList<Room>?> = _expandableList
    private var isDecreasing by mutableStateOf(false)

    fun fetchRooms() {
        CoroutineScope(Dispatchers.Default).launch {
            fetchRoomCall { rooms ->
                val groups = createGroups(roomsName)
                val elements = mutableMapOf<String, MutableList<Room>>().apply {
                    groups.forEach { group ->
                        put(group.title, mutableListOf())
                    }
                    rooms.forEach { room ->
                        groups.forEach { group ->
                            if (room.title.contains(group.title)) {
                                this[group.title]?.add(
                                    Room(
                                        title = getSimpleName(name = room.title),
                                        freetime = room.freetime
                                    )
                                )
                            }
                        }
                    }
                }
                // Tri des éléments
                elements.forEach { (_, list) ->
                    list.sortBy { if (isDecreasing) -it.freetime else it.freetime }
                }
                _expandableList.value = ExpandableList(groups, elements)
            }
        }
    }

    fun updateDecreasingOrder(decreasing: Boolean) {
        isDecreasing = decreasing
        fetchRooms()
    }
}


@Composable
fun getExpandableListRoom(): ExpandableList<Room>? {
    val groups = createGroups(roomsName)
    val elements = remember { mutableStateMapOf<String, MutableList<Room>>() }
    var expandableList by mutableStateOf<ExpandableList<Room>?>(null)

    LaunchedEffect(Unit) {
        fetchRoomCall { rooms ->
            elements.clear()
            groups.forEach { group ->
                elements[group.title] = mutableListOf()
            }
            rooms.forEach { room ->
                groups.forEach { group ->
                    if (room.title.contains(group.title)) {
                        elements[group.title]?.add(
                            Room(
                                title = getSimpleName(name = room.title),
                                freetime = room.freetime
                            )
                        )
                    }
                }
            }
            expandableList = ExpandableList(groups, elements)
        }
    }
    return expandableList
}

fun getSimpleName(name : String) : String {
    var regex = Regex(""".*""")
    if (name.contains(roomsName[0])) {
        regex = Regex("""^.*?([\d]+[A-Z]).*$""")
    } else if (name.contains(roomsName[1])) {
        regex = Regex("""^.*?([\d]{4}).*$""")
    } else if (name.contains(roomsName[2])){
        regex = Regex("""^([^ _]+)""")
    }
    val matchResult = regex.find(name)
    return matchResult?.groupValues?.get(1) ?: name
}


expect fun fetchRoomCall(callback: (List<Room>) -> Unit)