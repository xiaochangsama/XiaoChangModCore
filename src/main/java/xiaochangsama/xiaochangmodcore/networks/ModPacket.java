package xiaochangsama.xiaochangmodcore.networks;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import xiaochangsama.xiaochangmodcore.capabilities.PlayerProperties;

import java.util.function.Supplier;

//懒人一键同步玩家全部数据：ModMessages.sendToPlayer(new ModPacket(【要打包的playerProperty类型数据】),【对应的玩家(ServerPlayer) player】);
public class ModPacket {
    public PlayerProperties data;


    public ModPacket(FriendlyByteBuf buf) {
        this.data = new PlayerProperties();
        this.data.readNBT(buf.readNbt());

    }

    public ModPacket(PlayerProperties data) {
        this.data = data;
    }


    public static void buffer(ModPacket message, FriendlyByteBuf buffer) {
        buffer.writeNbt((CompoundTag) message.data.writeNBT());
    }


    //服务器对包的处理
    public static boolean handle(ModPacket message, Supplier<NetworkEvent.Context> supplier)//上下文支持
    {
        NetworkEvent.Context context = supplier.get();//获取参数
        context.enqueueWork(() -> {

            //以下是收到包后客户端会执行的函数
            if (!context.getDirection().getReceptionSide().isServer()) {
                PlayerProperties property = null;
                if (Minecraft.getInstance().player != null) {
                    property = ((PlayerProperties) Minecraft.getInstance().player.getCapability(PlayerProperties.PlayerPropertyProvider.PLAYER_PROPERTY, null).orElse(new PlayerProperties()));
                }
                if (property != null) {
                    property.copy(message.data);
                }
            }


        });
        return true;
    }


}
