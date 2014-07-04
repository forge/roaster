/*
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package com.test.domain;

@Entity
public class Goofy
{ @Id @GeneratedValue(strategy = GenerationType.AUTO) @Column(name = "id", updatable = false, nullable = false) private long id = 0; @Version @Column(name = "version") private int version = 0;    public long getId() { return this.id; }    public void setId(final long id) { this.id = id; }    public int getVersion() { return this.version; }    public void setVersion(final int version) { this.version = version; } }