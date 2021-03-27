#include <utility>

//
// Created by florian on 27.03.21.
//

#pragma once

namespace des {
  class basic_event : public event {
  public:
    basic_event(std::string n) :
        event(std::time(nullptr)), n_{std::move(n)} {}

  protected:
    [[nodiscard]] std::string name() const override {
      return "Basic Event: #" + n_;
    }

  private:
    std::string n_;
  };
}

