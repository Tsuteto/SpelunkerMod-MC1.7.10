package tsuteto.spelunker.sound;

public class SoundHandler
{
//    @SubscribeEvent
//    public void onSoundLoad(SoundLoadEvent event)
//    {
//        String[] extList = new String[]{"ogg", "wav"};
//
//        List<String> files = Lists.newArrayList();
//
//        files.add("1up");
//        files.add("2xscore_bgm");
//        files.add("2xscore");
//        files.add("coin");
//        files.add("death");
//        files.add("diamond");
//        files.add("dollar");
//        files.add("dynamite");
//        files.add("energy");
//        files.add("energyalert");
//        files.add("flare");
//        files.add("gameover");
//        files.add("gunshot");
//        files.add("invincible_bgm");
//        files.add("item");
//        files.add("jump");
//        files.add("main_bgm");
//        files.add("speedpotion_bgm");
//
//        for (String file : files)
//        {
//            try
//            {
//                for (String ext : extList)
//                {
//                    String srcpath = String.format("/assets/spelunker/sound/%s.%s", file, ext);
//                    URL url = SoundHandler.class.getResource(srcpath);
//                    if (url != null)
//                    {
//                        event.manager.addSound("spelunker:" + file + "." + ext);
//                    }
//                }
//            }
//            catch (Exception e)
//            {
//                ModLog.log(Level.SEVERE, e, "Failed to load sound file: %s", files);
//            }
//        }
//    }
}