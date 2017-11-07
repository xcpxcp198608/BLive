package com.wiatec.blive.pay

class PayResultInfo {

    var id: Int = 0
    var payerName: String? = null
    var publisherId: Int = 0
    var channelName: String? = null
    var auth: String? = null
    var paymentId: String? = null
    var state: String? = null
    var cart: String? = null
    var paymentMethod: String? = null
    var paymentStatus: String? = null
    var email: String? = null
    var firstName: String? = null
    var lastName: String? = null
    var payPalPayerId: String? = null
    var phone: String? = null
    var countryCode: String? = null
    var price: Float = 0.toFloat()
    var currency: String? = null
    var description: String? = null
    var transactionFee: Float = 0.toFloat()
    var createTime: String? = null
    var updateTime: String? = null
    var time: String? = null

    constructor() {}

    constructor(payerName: String, publisherId: Int) {
        this.payerName = payerName
        this.publisherId = publisherId
    }

    override fun toString(): String {
        return "PayResultInfo{" +
                "id=" + id +
                ", payerName='" + payerName + '\'' +
                ", publisherId=" + publisherId +
                ", channelName='" + channelName + '\'' +
                ", auth='" + auth + '\'' +
                ", paymentId='" + paymentId + '\'' +
                ", state='" + state + '\'' +
                ", cart='" + cart + '\'' +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", paymentStatus='" + paymentStatus + '\'' +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", payPalPayerId='" + payPalPayerId + '\'' +
                ", phone='" + phone + '\'' +
                ", countryCode='" + countryCode + '\'' +
                ", price=" + price +
                ", currency='" + currency + '\'' +
                ", description='" + description + '\'' +
                ", transactionFee=" + transactionFee +
                ", createTime='" + createTime + '\'' +
                ", updateTime='" + updateTime + '\'' +
                ", time='" + time + '\'' +
                '}'
    }
}
