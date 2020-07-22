package ru.alfomine.serverapi.sponge;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.group.Group;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.source.ConsoleSource;
import org.spongepowered.api.service.context.Context;
import org.spongepowered.api.service.permission.SubjectCollection;
import org.spongepowered.api.service.permission.SubjectData;
import org.spongepowered.api.service.permission.SubjectReference;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.channel.MessageChannel;
import org.spongepowered.api.util.Tristate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class ServerAPICommandSource implements ConsoleSource {
    private List<String> outputList = new ArrayList<>();
    private String name;
    private String groupName;

    public ServerAPICommandSource(String name, String groupName) {
        this.name = name;
        this.groupName = groupName;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void sendMessage(Text message) {
        outputList.add(message.toPlain());
    }

    public String getOutput() {
        return String.join("\n", outputList);
    }

    @Override
    public Tristate getPermissionValue(Set<Context> contexts, String permission) {
        if (groupName == null) return Tristate.TRUE;

        LuckPerms api = LuckPermsProvider.get();
        Group group = api.getGroupManager().getGroup(groupName);

        if (group == null) {
            System.out.println("Group " + groupName + " is null!");

            return Tristate.TRUE;
        }

        return Tristate.fromBoolean(group.getCachedData().getPermissionData(api.getContextManager().getStaticQueryOptions()).checkPermission(permission).asBoolean());
    }

    // Остальное не нужно, но нужно

    @Override
    public Optional<CommandSource> getCommandSource() {
        return Sponge.getServer().getConsole().getCommandSource();
    }

    @Override
    public SubjectCollection getContainingCollection() {
        return Sponge.getServer().getConsole().getContainingCollection();
    }

    @Override
    public SubjectReference asSubjectReference() {
        return Sponge.getServer().getConsole().asSubjectReference();
    }

    @Override
    public boolean isSubjectDataPersisted() {
        return Sponge.getServer().getConsole().isSubjectDataPersisted();
    }

    @Override
    public SubjectData getSubjectData() {
        return Sponge.getServer().getConsole().getSubjectData();
    }

    @Override
    public SubjectData getTransientSubjectData() {
        return Sponge.getServer().getConsole().getTransientSubjectData();
    }

    @Override
    public boolean isChildOf(Set<Context> contexts, SubjectReference parent) {
        return Sponge.getServer().getConsole().isChildOf(contexts, parent);
    }

    @Override
    public List<SubjectReference> getParents(Set<Context> contexts) {
        return Sponge.getServer().getConsole().getParents(contexts);
    }

    @Override
    public Optional<String> getOption(Set<Context> contexts, String key) {
        return Sponge.getServer().getConsole().getOption(key);
    }

    @Override
    public String getIdentifier() {
        return Sponge.getServer().getConsole().getIdentifier();
    }

    @Override
    public Set<Context> getActiveContexts() {
        return Sponge.getServer().getConsole().getActiveContexts();
    }

    @Override
    public MessageChannel getMessageChannel() {
        return Sponge.getServer().getConsole().getMessageChannel();
    }

    @Override
    public void setMessageChannel(MessageChannel channel) {

    }
}
