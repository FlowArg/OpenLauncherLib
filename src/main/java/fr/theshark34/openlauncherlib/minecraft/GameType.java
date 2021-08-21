/*
 * Copyright 2015-2017 Adrien "Litarvan" Navratil
 *
 * This file is part of the OpenLauncherLib.

 * The OpenLauncherLib is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * The OpenLauncherLib is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with the OpenLauncherLib.  If not, see <http://www.gnu.org/licenses/>.
 */

package fr.theshark34.openlauncherlib.minecraft;

import fr.flowarg.openlauncherlib.IForgeArgumentsProvider;
import fr.flowarg.openlauncherlib.ModifiedByFlow;
import fr.flowarg.openlauncherlib.NewForgeVersionDiscriminator;

import java.util.ArrayList;
import java.util.List;

/**
 * The Game Type
 *
 * <p>
 * This class contains the specifics informations about a version
 * or a group of verison of Minecraft.
 * <p>
 * It contains its main class, and its arguments.
 * </p>
 *
 * @author Litarvan
 * @version 3.0.4
 * @since 2.0.0-SNAPSHOT
 */
@ModifiedByFlow
public abstract class GameType implements IForgeArgumentsProvider
{
    private NewForgeVersionDiscriminator nfvd;

    /**
     * The 1.5.2 or Lower game type
     */
    public static final GameType V1_5_2_LOWER = new GameType()
    {
        @Override
        public String getName()
        {
            return "1.5.2 or lower";
        }

        @Override
        public String getMainClass(GameInfos infos)
        {
            return "net.minecraft.launchwrapper.Launch";
        }

        @Override
        public List<String> getLaunchArgs(GameInfos infos, GameFolder folder, AuthInfos authInfos)
        {
            final List<String> arguments = new ArrayList<>();

            arguments.add(authInfos.getUsername());

            arguments.add("token:" + authInfos.getAccessToken() + ":" + authInfos.getUuid());

            arguments.add("--gameDir");
            arguments.add(infos.getGameDir().toString());

            arguments.add("--assetsDir");
            arguments.add(infos.getGameDir().resolve(folder.getAssetsFolder()).resolve("virtual").resolve("legacy").toString());

            return arguments;
        }
    };

    /**
     * The 1.7.2 or Lower game type
     */
    public static final GameType V1_7_2_LOWER = new GameType()
    {
        @Override
        public String getName()
        {
            return "1.7.2 or lower";
        }

        @Override
        public String getMainClass(GameInfos infos)
        {
            return "net.minecraft.client.main.Main";
        }

        @Override
        public List<String> getLaunchArgs(GameInfos infos, GameFolder folder, AuthInfos authInfos)
        {
        	final List<String> arguments = new ArrayList<>();

            arguments.add("--username=" + authInfos.getUsername());

            arguments.add("--accessToken");
            arguments.add(authInfos.getAccessToken());

            arguments.add("--version");
            arguments.add(infos.getGameVersion().getName());

            arguments.add("--gameDir");
            arguments.add(infos.getGameDir().toString());

            arguments.add("--assetsDir");
            arguments.add(infos.getGameDir().resolve(folder.getAssetsFolder()).resolve("virtual").resolve("legacy").toString());

            arguments.add("--userProperties");
            arguments.add("{}");

            arguments.add("--uuid");
            arguments.add(authInfos.getUuid());

            arguments.add("--userType");
            arguments.add("legacy");

            return arguments;
        }
    };

    /**
     * The 1.7.10 Game Type
     */
    public static final GameType V1_7_10 = new GameType()
    {
        @Override
        public String getName()
        {
            return "1.7.10";
        }

        @Override
        public String getMainClass(GameInfos infos)
        {
            return "net.minecraft.client.main.Main";
        }

        @Override
        public List<String> getLaunchArgs(GameInfos infos, GameFolder folder, AuthInfos authInfos)
        {
            return getOldVanillaArguments(this, authInfos, folder, infos);
        }
    };

    /**
     * The 1.8 or higher Game Type
     */
    public static final GameType V1_8_HIGHER = new GameType()
    {
        @Override
        public String getName()
        {
            return "1.8 or higher";
        }

        @Override
        public String getMainClass(GameInfos infos)
        {
            return "net.minecraft.client.main.Main";
        }

        @Override
        public List<String> getLaunchArgs(GameInfos infos, GameFolder folder, AuthInfos authInfos)
        {
            return getOldVanillaArguments(this, authInfos, folder, infos);
        }
    };
    
    public static final GameType V1_13_HIGHER_FORGE = new GameType()
    {
        @Override
        public String getName()
        {
            return "1.13.x or higher with Forge";
        }
        
        @Override
        public String getMainClass(GameInfos infos)
        {
            return "cpw.mods.modlauncher.Launcher";
        }
        
        @Override
        public List<String> getLaunchArgs(GameInfos infos, GameFolder folder, AuthInfos authInfos)
        {
            final List<String> args = new ArrayList<>(getNewVanillaArguments(authInfos, folder, infos));
            if (this.getNFVD() == null)
                throw new IllegalStateException("You must set an instance of NewForgeVersionDiscriminator (NFVD)");
            args.addAll(this.getForgeArguments());
            return args;
        }
    };

    public static final GameType V1_13_HIGHER_VANILLA = new GameType()
    {
        @Override
        public String getName()
        {
            return "1.13.x or higher";
        }
        
        @Override
        public String getMainClass(GameInfos infos)
        {
            return "net.minecraft.client.main.Main";
        }
        
        @Override
        public List<String> getLaunchArgs(GameInfos infos, GameFolder folder, AuthInfos authInfos)
        {
            return getNewVanillaArguments(authInfos, folder, infos);
        }
    };
    
    public static final GameType V1_13_LOWER_FORGE = new GameType()
    {
        @Override
        public String getName()
        {
            return "1.12.x or lower forge";
        }
        
        @Override
        public String getMainClass(GameInfos infos)
        {
            return "net.minecraft.launchwrapper.Launch";
        }
        
        @Override
        public List<String> getLaunchArgs(GameInfos infos, GameFolder folder, AuthInfos authInfos)
        {
            return getOldForgeArguments(this, infos, folder, authInfos);
        }
    }
    
    public static final GameType FABRIC = new GameType()
    {
        @Override
        public String getName()
        {
            return "Fabric";
        }
        
        @Override
        public String getMainClass(GameInfos infos)
        {
            return "net.fabricmc.loader.launch.knot.KnotClient";
        }
        
        @Override
        public List<String> getLaunchArgs(GameInfos infos, GameFolder folder, AuthInfos authInfos)
        {
            return getNewVanillaArguments(authInfos, folder, infos);
        }
    };

    private static List<String> getOldVanillaArguments(GameType type, AuthInfos authInfos, GameFolder folder, GameInfos infos)
    {
        final List<String> arguments = new ArrayList<>();

        arguments.add("--username=" + authInfos.getUsername());

        arguments.add("--accessToken");
        arguments.add(authInfos.getAccessToken());

        if (authInfos.getClientToken() != null)
        {
            arguments.add("--clientToken");
            arguments.add(authInfos.getClientToken());
        }

        arguments.add("--version");
        arguments.add(infos.getGameVersion().getName());

        arguments.add("--gameDir");
        arguments.add(infos.getGameDir().toString());

        arguments.add("--assetsDir");
        arguments.add(infos.getGameDir().resolve(folder.getAssetsFolder()).toString());

        arguments.add("--assetIndex");
        arguments.add(getAssetIndex(type, infos.getGameVersion()));

        arguments.add("--userProperties");
        arguments.add("{}");

        arguments.add("--uuid");
        arguments.add(authInfos.getUuid());

        arguments.add("--userType");
        arguments.add("legacy");

        return arguments;
    }
    
    private static List<String> getOldForgeArguments(GameType type, AuthInfos authInfos, GameFolder folder, GameInfos infos) 
        final List<String> arguments = new ArrayList<>();

        arguments.add("--username" + authInfos.getUsername());

        arguments.add("--version");
        arguments.add(infos.getGameVersion().getName());

        arguments.add("--gameDir");
        arguments.add(infos.getGameDir().toString());

        arguments.add("--assetsDir");
        arguments.add(infos.getGameDir().resolve(folder.getAssetsFolder()).toString());
         
        arguments.add("--assetIndex");
        arguments.add(getAssetIndex(type, infos.getGameVersion())); 
        
        arguments.add("--uuid");
        arguments.add(authInfos.getUuid());
        
        arguments.add("--accessToken");
        arguments.add(authInfos.getAccessToken());
        
        arguments.add("--userType");
        arguments.add("legacy");
        
        arguments.add("--tweakClass net.minecraftforge.fml.common.launcher.FMLTweaker");
        
        arguments.add("--versionType Forge");
        
        /* I don't think we need that
        if (authInfos.getClientToken() != null)
        {
            arguments.add("--clientToken");
            arguments.add(authInfos.getClientToken());
        }

        arguments.add("--userProperties");
        arguments.add("{}");*/

        return arguments;
    }

    private static List<String> getNewVanillaArguments(AuthInfos authInfos, GameFolder folder, GameInfos infos)
    {
        final List<String> arguments = new ArrayList<>();
        arguments.add("--username");
        arguments.add(authInfos.getUsername());

        arguments.add("--version");
        arguments.add(infos.getGameVersion().getName());

        arguments.add("--gameDir");
        arguments.add(infos.getGameDir().toString());

        arguments.add("--assetsDir");
        arguments.add(infos.getGameDir().resolve(folder.getAssetsFolder()).toString());

        arguments.add("--assetIndex");
        arguments.add(infos.getGameVersion().getName().substring(0, infos.getGameVersion().getName().lastIndexOf('.')));

        arguments.add("--uuid");
        arguments.add(authInfos.getUuid());

        arguments.add("--accessToken");
        arguments.add(authInfos.getAccessToken());

        arguments.add("--userType");
        arguments.add("mojang");

        arguments.add("--versionType");
        arguments.add("release");
        return arguments;
    }
    
    /**
     * The name of the Game Type
     *
     * @return Returns the name of the game type
     */
    public abstract String getName();

    /**
     * Returns the main class of the Minecraft Game Type
     *
     * @param infos The infos of the game
     * @return The main class
     */
    public abstract String getMainClass(GameInfos infos);

    /**
     * Returns the launch arguments of the Minecraft Game Type
     *
     * @param infos     The infos of the game
     * @param folder    The current GameFolder
     * @param authInfos The current AuthInfos
     * @return The launch arguments
     */
    public abstract List<String> getLaunchArgs(GameInfos infos, GameFolder folder, AuthInfos authInfos);

    @Override
    public NewForgeVersionDiscriminator getNFVD()
    {
        return this.nfvd;
    }

    /**
     * Necessary if you want to launch a forge version 1.13.+.
     * @param nfvd a NFVD instance.
     * @deprecated use {@link #setNFVD(NewForgeVersionDiscriminator)} instead.
     *
     * @return this
     */
    @Deprecated
    public GameType setNewForgeVersionDiscriminator(NewForgeVersionDiscriminator nfvd)
    {
        this.nfvd = nfvd;
        return this;
    }

    /**
     * @param nfvd a NFVD instance.
     * Necessary if you want to launch a forge version 1.13.+.
     *
     * @return this
     */
    public GameType setNFVD(NewForgeVersionDiscriminator nfvd)
    {
        this.nfvd = nfvd;
        return this;
    }

    private static String getAssetIndex(GameType type, GameVersion gameVersion)
    {
        if(type == GameType.V1_8_HIGHER)
        {
            String version = gameVersion.getName();

            final int first  = version.indexOf('.');
            final int second = version.lastIndexOf('.');

            if (first != second) version = version.substring(0, version.lastIndexOf('.'));

            if (gameVersion.getName().equals("1.13.1") || gameVersion.getName().equals("1.13.2"))
                version = "1.13.1";

            return version;
        }
        else return gameVersion.getName();
    }
    
    @Override
    public boolean equals(Object obj)
    {
        if(obj instanceof GameType)
        {
            final GameType o = (GameType)obj;
            return o.getName().equals(this.getName());
        }
        else return false;
    }
}
