//
// Created by florian on 27.03.21.
//

#pragma once

#include <iostream>
#include <queue>
#include <memory>

namespace des {
  class simulator {
  public:

    using event_ptr = std::shared_ptr<event>;

    simulator(std::initializer_list<event_ptr> init_list) {
      for(const auto& item : init_list) {
        this->events_.push(item);
      }
    }

    void run() {
      while (!events_.empty()) {
        event_ptr e = events_.top();
        std::cout << (*e) << std::endl;
        events_.pop();
      }
    }

  private:
    std::priority_queue<event_ptr,
        std::vector<event_ptr>,
        event::bigger_than_comparator> events_;
  };
}

