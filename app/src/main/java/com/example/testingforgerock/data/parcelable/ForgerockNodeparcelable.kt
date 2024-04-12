import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import org.forgerock.android.auth.Node
import org.forgerock.android.auth.callback.Callback

@Parcelize
data class ForgerockNodeParcelable(
    var node: Node
) : Parcelable
