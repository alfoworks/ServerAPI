package ru.alfomine.serverapi.sponge;

import org.apache.commons.lang3.ArrayUtils;
import org.spongepowered.api.Platform;
import org.spongepowered.api.network.ChannelBuf;
import org.spongepowered.api.network.PlayerConnection;
import org.spongepowered.api.network.RawDataListener;
import org.spongepowered.api.network.RemoteConnection;

import static java.lang.Math.min;

public class ScreenshotListener implements RawDataListener {
    boolean ok = false;
    byte[] result = new byte[]{};

    @Override
    public void handlePayload(ChannelBuf data, RemoteConnection connection, Platform.Type side) {
        if (!(connection instanceof PlayerConnection)) {
            return;
        }

        boolean isEnd = data.readBoolean();

        if (isEnd) {
            ok = true;
        } else {
            byte[] chunkByteArray = data.readBytes(min(10240, data.available()));
            byte[] prevArr = result;

            result = ArrayUtils.addAll(prevArr, chunkByteArray);
        }
    }
}
