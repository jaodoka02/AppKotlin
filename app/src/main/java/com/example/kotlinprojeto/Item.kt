import android.os.Parcel
import android.os.Parcelable

data class Item(
    var isPurchased: Boolean = false,
    val category: String,
    val unit: String,
    val quantity: Int,
    val name: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readByte() != 0.toByte(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeByte(if (isPurchased) 1 else 0)
        parcel.writeString(category)
        parcel.writeString(unit)
        parcel.writeInt(quantity)
        parcel.writeString(name)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<Item> {
        override fun createFromParcel(parcel: Parcel): Item = Item(parcel)
        override fun newArray(size: Int): Array<Item?> = arrayOfNulls(size)
    }
}
