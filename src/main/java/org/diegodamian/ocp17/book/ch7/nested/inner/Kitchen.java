package org.diegodamian.ocp17.book.ch7.nested.inner;

import org.diegodamian.ocp17.book.ch7.nested.inner.Kitchen.Mixer.Spinner;

public class Kitchen {

  public void bake() {
    Spinner f = new Kitchen().new Mixer().new Spinner();
  }

  class Mixer {

    class Spinner {

    }
  }
}
