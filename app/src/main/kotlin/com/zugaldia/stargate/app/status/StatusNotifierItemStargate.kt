package com.zugaldia.stargate.app.status

import com.zugaldia.stargate.sdk.status.StargateMenu
import com.zugaldia.stargate.sdk.status.StargateStatusNotifierItem
import org.kde.PropertyToolTipStruct

class StatusNotifierItemStargate(
    menu: StargateMenu?,
    onActivate: (token: String?) -> Unit,
    onSecondaryActivate: (token: String?) -> Unit,
) : StargateStatusNotifierItem(menu, onActivate, onSecondaryActivate) {
    override fun getId(): String = "stargate"
    override fun getTitle(): String = "Stargate"
    override fun getIconName(): String = "dialog-information"
    override fun getToolTipInfo(): Pair<String, String> = Pair("Stargate", "XDG Desktop Portal Integration")
}
