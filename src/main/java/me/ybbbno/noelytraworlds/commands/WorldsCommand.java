package me.ybbbno.noelytraworlds.commands;

import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import me.deadybbb.ybmj.LegacyTextHandler;
import me.ybbbno.noelytraworlds.NoElytraWorlds;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;

public class WorldsCommand implements BasicCommand {
    private final NoElytraWorlds plugin;

    public WorldsCommand(NoElytraWorlds plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSourceStack source, String[] args) {
        CommandSender s = source.getSender();

        if (args[0].equals("add")) {
            if (plugin.handler.addWorld(args[1])) {
                LegacyTextHandler.sendFormattedMessage(s, "<green>Мир был добавлен в конфиг!");
            } else {
                LegacyTextHandler.sendFormattedMessage(s, "<red>Такого мира не существует!");
            }
            return;
        } else if (args[0].equals("remove")) {
            if (plugin.handler.removeWorld(args[1])) {
                LegacyTextHandler.sendFormattedMessage(s, "<green>Мир был удалён из конфига!");
            } else {
                LegacyTextHandler.sendFormattedMessage(s, "<red>Такого мира не существует!");
            }
            return;
        }

        LegacyTextHandler.sendFormattedMessage(s, "Для выполнения команды отправьте /world add/remove <world>");
    }

    @Override
    public Collection<String> suggest(final CommandSourceStack source, final String[] args) {
        if (args.length == 0) {
            return List.of("add", "remove");
        }
        return Bukkit.getWorlds().stream()
                .map(World::getName)
                .filter(name -> name.toLowerCase()
                        .startsWith(args[args.length - 1]
                                .toLowerCase()))
                .toList();
    }

    @Override
    public boolean canUse(final CommandSender sender) {
        final String permission = this.permission();
        return sender.hasPermission(permission) &&
                sender instanceof Player;
    }

    @Override
    public @Nullable String permission() { return "noelytraworlds.use"; }
}
