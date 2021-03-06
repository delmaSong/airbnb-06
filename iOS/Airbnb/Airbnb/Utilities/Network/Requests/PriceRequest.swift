//
//  PriceRequest.swift
//  Airbnb
//
//  Created by delma on 2020/06/05.
//  Copyright © 2020 jinie. All rights reserved.
//

import Foundation
import Alamofire

final class PriceDetailRequest: Request, URLRequestConvertible {
  var path: String = EndPoints.defaultURL + EndPoints.priceGraph
  var headers: HTTPHeaders?
  func asURLRequest() -> URLRequest {
    var request = URLRequest(url: URL(string: path)!)
    guard let headersWithToken = setToken() else { return request }
    request.headers = headersWithToken
    return request
  }
}

