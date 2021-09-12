/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.krakenapp;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author Wilson
 */
class Result {
    
    @JsonProperty("XETHZUSD")
    private Pair pair;

    public Pair getPair() {
        return pair;
    }

    public void setPair(Pair pair) {
        this.pair = pair;
    }
    
    
}
