package com.airline.reservation.util;

import javafx.animation.*;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.effect.InnerShadow;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class AirLinkAnimations {

    // TRANSITIONS

    public static void fadeIn(Node node, int durationMs) {
        if (node == null) return;

        node.setOpacity(0);
        FadeTransition ft = new FadeTransition(Duration.millis(durationMs), node);
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.play();
    }

    public static void fadeOut(Node node, int durationMs, Runnable after) {
        if (node == null) return;

        FadeTransition ft = new FadeTransition(Duration.millis(durationMs), node);
        ft.setFromValue(1);
        ft.setToValue(0);
        ft.setOnFinished(e -> {
            if (after != null) after.run();
        });
        ft.play();
    }

    public static void slideIn(Node node, int durationMs) {
        if (node == null) return;

        node.setTranslateX(60);
        node.setOpacity(0);

        FadeTransition fade = new FadeTransition(Duration.millis(durationMs), node);
        fade.setFromValue(0);
        fade.setToValue(1);

        TranslateTransition slide = new TranslateTransition(Duration.millis(durationMs), node);
        slide.setFromX(60);
        slide.setToX(0);

        new ParallelTransition(fade, slide).play();
    }

    public static void slideUp(Node node, int durationMs) {
        if (node == null) return;

        node.setTranslateY(40);
        node.setOpacity(0);

        TranslateTransition slide = new TranslateTransition(Duration.millis(durationMs), node);
        slide.setFromY(40);
        slide.setToY(0);

        FadeTransition fade = new FadeTransition(Duration.millis(durationMs), node);
        fade.setFromValue(0);
        fade.setToValue(1);

        new ParallelTransition(slide, fade).play();
    }

    public static void zoomIn(Node node, int durationMs) {
        if (node == null) return;

        node.setScaleX(0.7);
        node.setScaleY(0.7);
        node.setOpacity(0);

        ScaleTransition zoom = new ScaleTransition(Duration.millis(durationMs), node);
        zoom.setFromX(0.7);
        zoom.setFromY(0.7);
        zoom.setToX(1);
        zoom.setToY(1);

        FadeTransition fade = new FadeTransition(Duration.millis(durationMs), node);
        fade.setFromValue(0);
        fade.setToValue(1);

        new ParallelTransition(zoom, fade).play();
    }

    public static void bounce(Node node) {
        if (node == null) return;

        ScaleTransition st = new ScaleTransition(Duration.millis(160), node);
        st.setFromX(1);
        st.setToX(1.12);
        st.setFromY(1);
        st.setToY(1.12);
        st.setCycleCount(2);
        st.setAutoReverse(true);
        st.play();
    }

    public static void glow(Node node) {
        if (node == null) return;

        InnerShadow glow = new InnerShadow();
        glow.setColor(Color.web("#00FFAA"));
        glow.setRadius(0);
        node.setEffect(glow);

        Timeline tl = new Timeline(new KeyFrame(Duration.millis(220),
                new KeyValue(glow.radiusProperty(), 20)));
        tl.play();
    }

    // ANIMATIONS

    public static void shake(Node node, int durationMs) {
        if (node == null) return;

        TranslateTransition shake = new TranslateTransition(Duration.millis(durationMs), node);
        shake.setFromX(0);
        shake.setByX(10);
        shake.setCycleCount(4);
        shake.setAutoReverse(true);
        shake.setOnFinished(e -> node.setTranslateX(0));
        shake.play();
    }

    public static void pulse(Node node, int durationMs) {
        if (node == null) return;

        ScaleTransition pulse = new ScaleTransition(Duration.millis(durationMs), node);
        pulse.setFromX(1);
        pulse.setToX(1.05);
        pulse.setFromY(1);
        pulse.setToY(1.05);
        pulse.setCycleCount(2);
        pulse.setAutoReverse(true);
        pulse.play();
    }

    // TEXT ANIMATIONS

    public static void animateCounter(Label label, int target) {
        if (label == null) return;

        IntegerProperty value = new SimpleIntegerProperty(0);
        label.textProperty().bind(value.asString());
        Timeline tl = new Timeline(new KeyFrame(Duration.millis(1200),
                new KeyValue(value, target, Interpolator.EASE_OUT)));
        tl.play();
    }

    // FOCUS EFFECTS

    public static void focusGlow(Node node) {
        if (node == null) return;

        InnerShadow glow = new InnerShadow();
        glow.setColor(Color.web("#667eea"));
        glow.setRadius(0);
        node.setEffect(glow);

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.millis(200),
                        new KeyValue(glow.radiusProperty(), 10))
        );
        timeline.play();
    }

    public static void removeGlow(Node node) {
        if (node == null) return;

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.millis(200),
                        new KeyValue(node.effectProperty(), null))
        );
        timeline.play();
    }
}

