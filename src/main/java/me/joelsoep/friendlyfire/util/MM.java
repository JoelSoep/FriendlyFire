package me.joelsoep.friendlyfire.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class MM {
    public static Component deserialize(String input) {
        return MiniMessage.miniMessage().deserialize("<!i>" + input);
    }
}
