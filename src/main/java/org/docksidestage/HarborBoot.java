/*
 * Copyright 2015-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.docksidestage;

/**
 * @author jflute
 */
public class HarborBoot { // #change_it_first

    // JettyBoot使うとTomcatで起動時にエラーになるのでJettyの依存は除外する
    // 時間があればTomcatBootに変更.
    public static void main(String[] args) { // e.g. java -Dlasta.env=production -jar harbor.war
        System.err.println("Run with docker, not support Jetty Boot ");
        // new JettyBoot(8090, "/harbor").asDevelopment(isDevelopment()).bootAwait();
    }

    //    private static boolean isDevelopment() {
    //        return System.getProperty("lasta.env") == null;
    //    }
}
