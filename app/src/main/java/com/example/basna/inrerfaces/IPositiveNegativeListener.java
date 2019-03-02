package com.example.basna.inrerfaces;

@FunctionalInterface
public interface IPositiveNegativeListener {

    void onPositive();

    default void onNegative() {

    }
}