package xiaochangsama.xiaochangmodcore;

import net.minecraftforge.common.ForgeConfigSpec;


//forge1.19.x有关config的文档：https://docs.minecraftforge.net/en/latest/misc/config/
public class ModConfig {
    public static final ForgeConfigSpec CONFIG;
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    //这里是所有需要定义的配置内容
    /*
    public static final ForgeConfigSpec.ConfigValue<Float> ARM_LENGTH;
    public static final ForgeConfigSpec.ConfigValue<Integer> BATTLE_TIME;
    public static final ForgeConfigSpec.ConfigValue<Integer> HUD_TIME;

     */

    static {//java基础知识小贴士：这个块内装入只用被定义一次的内容可以提升性能，不然可能会导致每次调用产生新对象
        BUILDER.push("这是XiaoChang Mod Core前置模组的配置文件");
        BUILDER.push("================其他配置================");
        /*
        //在这里配置所有的配置内容
        ARM_LENGTH = BUILDER.comment("玩家基本的手长")
                //.worldRestart()这是用来定义其在世界重启的时候才会重新加载
                //.translation("arm_length")这是定义它的翻译键
                .define("Arm Length", 1f);
        //调用方式ModConfig.ARM_LENGTH.get()
        BUILDER.push("================其他部分================");
        BATTLE_TIME = BUILDER.comment("战斗状态持续时长(刻数)").define("Battle Time", 160);
        HUD_TIME = BUILDER.comment("战斗状态过后,HUD显示的时长(刻数)").define("HUD Time", 40);


         */

        BUILDER.pop();//不知道是什么意思，有人知道的话给我留言啊
        CONFIG = BUILDER.build();
    }
}