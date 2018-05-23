/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package as.pa2.server;

/**
 *
 * @author omp
 * Leibniz formula for PI
 * PI=4*(1-1/3+1/5-1/7+1/9-1/11 + ... +((-1)^(n+1))/(2n - 1))
 */
public class Pi {

    // iterations: num of iterations
    // delay: delay in seconds to simulate computational time
    public Double compute(Long iterations, int delay) {
    
        double pi = 0;
        double denominator = 1;
 
        for (int n = 0; n < iterations; n++) {
            pi = (n%2==0)? pi + (1./denominator): pi - (1./denominator);
            denominator += 2;
        }
        try {
            Thread.sleep(delay * 1000);
        } catch (Exception e) {
        }
        return pi * 4;
    }

}
