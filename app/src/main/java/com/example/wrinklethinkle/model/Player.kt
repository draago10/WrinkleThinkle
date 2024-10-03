import android.os.Parcel
import android.os.Parcelable

data class Player(
    var name: String = "",
    var level: Int = 1,             // Player starts at level 1
    var gold: Int = 0,              // Player starts with 0 gold
    var clickPower: Double = 1.0,   // Base click power, increases with level
    var experience: Int = 0,        // Player's current experience
    var inventory: MutableList<String> = mutableListOf()   // Player's inventory
) : Parcelable {

    fun expToNextLevel(): Int {
        return 100 * level
    }

    fun gainExperience(exp: Int) {
        experience += exp
        if (experience >= expToNextLevel()) {
            levelUp()
        }
    }

    private fun levelUp() {
        level++
        experience = 0
        clickPower += 0.1
    }

    // Parcelable implementation
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readInt(),
        parcel.readDouble(),
        parcel.readInt(),
        parcel.createStringArrayList() as MutableList<String>
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeInt(level)
        parcel.writeInt(gold)
        parcel.writeDouble(clickPower)
        parcel.writeInt(experience)
        parcel.writeStringList(inventory)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Player> {
        override fun createFromParcel(parcel: Parcel): Player {
            return Player(parcel)
        }

        override fun newArray(size: Int): Array<Player?> {
            return arrayOfNulls(size)
        }
    }
}
