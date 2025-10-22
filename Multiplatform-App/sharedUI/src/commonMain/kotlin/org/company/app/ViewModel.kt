import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.company.app.network.Room
import org.company.app.network.fetchRoomCall
import org.company.app.network.getSimpleName
import org.company.app.network.roomsName
import org.company.app.utilsUI.ExpandableList
import org.company.app.utilsUI.createGroups

class RoomViewModel {
    private val _expandableList = mutableStateOf<ExpandableList<Room>?>(null)
    val expandableList: State<ExpandableList<Room>?> = _expandableList

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
                _expandableList.value = ExpandableList(groups, elements)
            }
        }
    }
}
