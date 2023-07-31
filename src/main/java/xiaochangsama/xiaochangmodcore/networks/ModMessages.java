package xiaochangsama.xiaochangmodcore.networks;


import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import xiaochangsama.xiaochangmodcore.XiaoChangModCore;

public class ModMessages {
    //来自forge的简单数据包实例
    private static SimpleChannel INSTANCE;
    //自增的数据包编号
    private static int packetId = 0;

    private static int id() {
        return packetId++;
    }

    //数据包的注册方法
    public static void register() {
        SimpleChannel net = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(XiaoChangModCore.MODID, "message"))//本地化的数据包名
                .networkProtocolVersion(() -> "1.0")//服务器版本
                .clientAcceptedVersions(s -> true)//客户端可以接收
                .serverAcceptedVersions(s -> true)//服务端可以接收
                .simpleChannel();
        INSTANCE = net;//赋值，实例的内容
//在这里进行包的注册
        net.messageBuilder(ModPacket.class,//数据包的类
                        id(),//唯一的数据包id
                        NetworkDirection.PLAY_TO_CLIENT//服务端发送给客户端
                )
                .decoder(ModPacket::new)//解码器
                .encoder(ModPacket::buffer)//编码器
                .consumerMainThread((ModPacket::handle))//回调函数（接收到包后执行的操作）
                .add();


    }

    //客户端发送给服务器的数据包，传入MSG型数据
    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }

    //服务器发送给客户端的数据包,传入MSG型数据，与服务器中的玩家对象
    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }
}
