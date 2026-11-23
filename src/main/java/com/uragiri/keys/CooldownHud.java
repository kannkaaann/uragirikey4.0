package com.uragiri.keys;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.scoreboard.ReadableScoreboardScore;
import net.minecraft.scoreboard.ScoreHolder;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardObjective;

/** 裏切り者の画面の右側に、各スキルのクールダウン状況を表示する。 */
public final class CooldownHud {
    private static final String[][] ROWS = {
        {"lockon", "ロックオン"}, {"zombie", "ゾンビ"}, {"randomtp", "場所入替"},
        {"invisibility", "透明化"}, {"scatter", "バラバラ"}, {"gag", "口封じ"},
        {"deathsentence", "死の宣告"}, {"baggage", "手荷物"}
    };

    private CooldownHud() {}

    private static int read(Scoreboard sb, ScoreboardObjective obj, String name) {
        ReadableScoreboardScore s = sb.getScore(ScoreHolder.fromName(name), obj);
        return s == null ? 0 : s.getScore();
    }

    private static String fmt(int sec) {
        return (sec / 60) + ":" + String.format("%02d", sec % 60);
    }

    public static void render(DrawContext ctx, RenderTickCounter tickCounter) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null || mc.world == null || mc.options.hudHidden) {
            return;
        }
        if (!Impersonation.inTraitorTeam(mc.player)) {
            return;
        }
        Scoreboard sb = mc.world.getScoreboard();
        ScoreboardObjective obj = sb.getNullableObjective("cdhud");
        if (obj == null) {
            return;
        }
        TextRenderer tr = mc.textRenderer;
        int x = ctx.getScaledWindowWidth() - 6;
        int y = ctx.getScaledWindowHeight() / 2 - ROWS.length * 5;
        for (String[] r : ROWS) {
            int sec = read(sb, obj, r[0]);
            String label;
            int color;
            if (r[0].equals("baggage")) {
                int left = read(sb, obj, "baggage_left");
                if (left <= 0) {
                    label = r[1] + " 使用不可";
                    color = 0xAAAAAA;
                } else if (sec > 0) {
                    label = r[1] + " " + fmt(sec) + " (残" + left + ")";
                    color = 0xFF5555;
                } else {
                    label = r[1] + " OK (残" + left + ")";
                    color = 0x55FF55;
                }
            } else if (sec > 0) {
                label = r[1] + " " + fmt(sec);
                color = 0xFF5555;
            } else {
                label = r[1] + " OK";
                color = 0x55FF55;
            }
            int w = tr.getWidth(label);
            ctx.drawTextWithShadow(tr, label, x - w, y, color);
            y += 10;
        }
    }
}
