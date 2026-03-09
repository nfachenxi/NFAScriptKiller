package com.chenxi.scriptkiller.voicechat.enhancedgroups.command;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import de.maxhenkel.voicechat.api.Group;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class GroupTypeArgument implements ArgumentType<Group.Type> {

    private static final List<String> EXAMPLES = Arrays.asList("normal", "open", "isolated");

    public static GroupTypeArgument groupType() {
        return new GroupTypeArgument();
    }

    public static Group.Type getGroupType(CommandContext<CommandSourceStack> context, String name) {
        return context.getArgument(name, Group.Type.class);
    }

    @Override
    public Group.Type parse(StringReader reader) throws CommandSyntaxException {
        String input = reader.readUnquotedString().toLowerCase();
        return switch (input) {
            case "normal" -> Group.Type.NORMAL;
            case "open" -> Group.Type.OPEN;
            case "isolated" -> Group.Type.ISOLATED;
            default -> throw new SimpleCommandExceptionType(Component.literal("Invalid group type: " + input + ". Valid types: normal, open, isolated")).create();
        };
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        builder.suggest("normal");
        builder.suggest("open");
        builder.suggest("isolated");
        return builder.buildFuture();
    }

    @Override
    public Collection<String> getExamples() {
        return EXAMPLES;
    }
}
