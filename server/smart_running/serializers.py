from django.contrib.auth.models import User
from rest_auth.app_settings import UserDetailsSerializer
from rest_framework import serializers

from smart_running.models import Route, Marker, UserProfile


class RouteSerializer(serializers.HyperlinkedModelSerializer):
    class Meta:
        model = Route
        fields = ('url', 'publisher', 'date_published', 'last_updated', 'title', 'description',
                  'mode', 'difficulty', 'markers')  # markers too
        read_only_fields = ('publisher', 'date_published', 'last_updated')
        # TODO set read only fields on create()


class UserSerializer(UserDetailsSerializer):
    email_verified = serializers.BooleanField(source="userprofile.email_verified")

    class Meta(UserDetailsSerializer.Meta):
        fields = UserDetailsSerializer.Meta.fields + ('email_verified',)

    def update(self, instance, validated_data):
        profile_data = validated_data.pop('userprofile', {})
        email_verified = profile_data.get('email_verified')

        instance = super(UserSerializer, self).update(instance, validated_data)

        # get and update user profile
        profile = instance.userprofile
        if profile_data and email_verified:
            profile.email_verified = email_verified
            profile.save()
        return instance


class MarkerSerializer(serializers.HyperlinkedModelSerializer):
    class Meta:
        model = Marker
        fields = ('url', 'title', 'description', 'full_description', 'latitude',
                  'longitude', 'zone_radius')
